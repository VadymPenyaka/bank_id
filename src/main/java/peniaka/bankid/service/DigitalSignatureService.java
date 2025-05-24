package peniaka.bankid.service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import peniaka.bankid.exception.NotFoundException;
import peniaka.bankid.model.PassportDTO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DigitalSignatureService {
    private final SignGeneratorService signGeneratorService;
    private final PersonService personService;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public byte[] signPdf(MultipartFile file, UUID personId) throws Exception {
        byte[] pdfBytes = file.getBytes();
        PassportDTO passport = extractPassportData(personId);
        String personData = String.valueOf(passport.getDocumentNumber());

        byte[] seed1 = deriveSeedFromPassport(personData);

        KeyPair keyPair1 = signGeneratorService.generateDeterministicECKeyPair(seed1);

        X509Certificate certificate1 = signGeneratorService.generateSelfSignedCertificate(keyPair1, personData);


        try (ByteArrayInputStream input = new ByteArrayInputStream(pdfBytes);
             ByteArrayOutputStream signedPdf = new ByteArrayOutputStream()) {

            PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(input));

            PDSignature signature1 = createSignatureMetadata();

            document.addSignature(signature1, createSignatureInterface(certificate1, keyPair1, signature1));
            setupSignatureField(document, signature1, passport.getFullName());

            document.saveIncremental(signedPdf);
            return signedPdf.toByteArray();
        }
    }

    private PassportDTO extractPassportData(UUID personId) {
        return personService.getPersonById(personId).orElseThrow(()
                -> new NotFoundException("Person not found!")).getPassport();
    }

    private byte[] deriveSeedFromPassport(String data) {
        return data.getBytes(StandardCharsets.UTF_8);
    }

    private PDSignature createSignatureMetadata() {
        PDSignature signature = new PDSignature();
        signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
        signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
        signature.setName("Digital Signature");
        signature.setLocation("Educational Project");
        signature.setReason("Document Authentication");
        signature.setSignDate(Calendar.getInstance());
        return signature;
    }

    private SignatureInterface createSignatureInterface(X509Certificate certificate, KeyPair keyPair, PDSignature signature) {
        return content -> {
            try {
                java.security.cert.Certificate[] certChain = new java.security.cert.Certificate[]{certificate};

                CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
                ContentSigner sha256Signer = new JcaContentSignerBuilder("SHA256withECDSA")
                        .setProvider("BC").build(keyPair.getPrivate());

                gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
                        new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
                        .build(sha256Signer, certificate));

                gen.addCertificates(new JcaCertStore(List.of(certChain)));

                CMSProcessableByteArray processable = new CMSProcessableByteArray(content.readAllBytes());
                CMSSignedData signedData = gen.generate(processable, false);

                return signedData.getEncoded();
            } catch (Exception e) {
                throw new IOException("Signing failed", e);
            }
        };
    }

    private void setupSignatureField(PDDocument document, PDSignature signature, String fullName) throws IOException {
        PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
        if (acroForm == null) {
            acroForm = new PDAcroForm(document);
            document.getDocumentCatalog().setAcroForm(acroForm);
        }

        int lastPageIndex = document.getNumberOfPages() - 1;
        PDPage page = document.getPage(lastPageIndex);
        PDRectangle rect = new PDRectangle(page.getMediaBox().getWidth() - 250, 230, 200, 80);

        PDSignatureField signatureField = new PDSignatureField(acroForm);
        PDAnnotationWidget widget = signatureField.getWidgets().get(0);
        widget.setRectangle(rect);
        widget.setPage(page);
        widget.setPrinted(true);
        signatureField.setValue(signature);
        signatureField.getCOSObject().setItem(COSName.V, signature);

        setupSignatureAppearance(document, widget, rect, fullName);

        acroForm.getFields().add(signatureField);
        page.getAnnotations().add(widget);
    }

    private void setupSignatureAppearance(PDDocument document, PDAnnotationWidget widget, PDRectangle rect, String signedBy) throws IOException {
        PDAppearanceDictionary appearanceDict = new PDAppearanceDictionary();
        PDAppearanceStream appearanceStream = new PDAppearanceStream(document);
        PDRectangle bbox = new PDRectangle(rect.getWidth(), rect.getHeight());

        appearanceStream.setBBox(bbox);
        appearanceStream.setResources(new PDResources());

        COSArray matrix = new COSArray();
        matrix.add(COSInteger.get(1));
        matrix.add(COSInteger.get(0));
        matrix.add(COSInteger.get(0));
        matrix.add(COSInteger.get(1));
        matrix.add(COSInteger.get(0));
        matrix.add(COSInteger.get(0));
        appearanceStream.getCOSObject().setItem(COSName.MATRIX, matrix);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, appearanceStream)) {
            float width = bbox.getWidth();
            float height = bbox.getHeight();

            contentStream.setNonStrokingColor(0.9f, 0.9f, 1.0f);
            contentStream.addRect(0, 0, width, height);
            contentStream.fill();

            contentStream.setStrokingColor(0.2f, 0.2f, 0.8f);
            contentStream.setLineWidth(2);
            contentStream.addRect(1, 1, width - 2, height - 2);
            contentStream.stroke();

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.newLineAtOffset(8, height - 15);
            contentStream.showText("DIGITALLY SIGNED");

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            contentStream.newLineAtOffset(0, -12);
            contentStream.showText("By: " + signedBy);
            contentStream.newLineAtOffset(0, -12);
            contentStream.showText("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            contentStream.newLineAtOffset(0, -12);
            contentStream.showText("Status: Digitally Signed");
            contentStream.endText();
        }

        appearanceDict.setNormalAppearance(appearanceStream);
        widget.setAppearance(appearanceDict);
    }
}
