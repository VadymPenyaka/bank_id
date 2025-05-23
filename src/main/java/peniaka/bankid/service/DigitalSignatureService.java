package peniaka.bankid.service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.visible.PDVisibleSigProperties;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.visible.PDVisibleSignDesigner;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import peniaka.bankid.exception.NotFoundException;
import peniaka.bankid.model.PersonDTO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.UUID;
//TODO make signature visual
@Service
@RequiredArgsConstructor
public class DigitalSignatureService {
    private final SignGeneratorService signGeneratorService;
    private final PersonService personService;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

//    public byte[] signPdf(MultipartFile file, UUID personId) throws Exception {
//        byte[] pdfBytes = file.getBytes();
//        PersonDTO personDTO = extractPassportData(personId);
//        String personData = String.valueOf(personDTO.hashCode());
//        byte[] seed = deriveSeedFromPassport(personData);
//        KeyPair keyPair = signGeneratorService.generateDeterministicECKeyPair(seed);
//
//        X509Certificate certificate = signGeneratorService.generateSelfSignedCertificate(keyPair, personData);
//
//        ByteArrayOutputStream signedPdf = new ByteArrayOutputStream();
//
//        try (ByteArrayInputStream input = new ByteArrayInputStream(pdfBytes)) {
//            RandomAccessReadBuffer randomAccess = new RandomAccessReadBuffer(input);
//            PDDocument document = Loader.loadPDF(randomAccess);
//            PDSignature signature = new PDSignature();
//            signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
//            signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
//            signature.setName("User Signature");
//            signature.setLocation("Educational Project");
//            signature.setSignDate(Calendar.getInstance());
//
//            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
//            if (acroForm == null) {
//                acroForm = new PDAcroForm(document);
//                document.getDocumentCatalog().setAcroForm(acroForm);
//            }
//
//            PDSignatureField signatureField = new PDSignatureField(acroForm);
//            signatureField.getCOSObject().setItem(COSName.V, signature);
//            acroForm.getFields().add(signatureField);
//
//            PDPage page = document.getPage(0);
//            PDRectangle mediaBox = page.getMediaBox();  // <-- додано: отримати розмір сторінки
//            float rectWidth = 200;
//            float rectHeight = 50;
//            float x = mediaBox.getWidth() - rectWidth - 10;  // <-- додано: X — відступ 10 пікселів від правого краю
//            float y = 10;  // <-- додано: Y — 10 пікселів від низу сторінки
//
//            PDRectangle rect = new PDRectangle(x, y, rectWidth, rectHeight); // вибираємо першу сторінку для підпису
//
//            PDAnnotationWidget widget = signatureField.getWidgets().get(0);
//            widget.setRectangle(rect);
//            widget.setPage(page);
//            page.getAnnotations().add(widget);
//            PDAppearanceDictionary appearanceDict = new PDAppearanceDictionary();
//            PDAppearanceStream appearanceStream = new PDAppearanceStream(document);
//
//            appearanceStream.setResources(new PDResources());
//            appearanceStream.setBBox(new PDRectangle(rect.getWidth(), rect.getHeight()));
//
//            try (PDPageContentStream contentStream = new PDPageContentStream(document, appearanceStream)) {
//                contentStream.beginText();
//                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
//                contentStream.newLineAtOffset(10, 25);
//                contentStream.setNonStrokingColor(1.0f, 0, 0);
//                contentStream.showText("Signed by "+personDTO.getPassport().getFullName());
//                contentStream.endText();
//            }
//
//            appearanceDict.setNormalAppearance(appearanceStream);
//            widget.setAppearance(appearanceDict);
//
//            document.addSignature(signature, new SignatureInterface() {
//                @Override
//                public byte[] sign(InputStream content) throws IOException {
//                    try {
//                        Signature signer = Signature.getInstance("SHA256withECDSA", "BC");
//                        signer.initSign(keyPair.getPrivate());
//                        byte[] buffer = content.readAllBytes();
//                        signer.update(buffer);
//                        return signer.sign();
//                    } catch (Exception e) {
//                        throw new IOException("Signing failed", e);
//                    }
//                }
//            });
//
//            document.saveIncremental(signedPdf);
//        }
//
//        return signedPdf.toByteArray();
//    }

    public byte[] signPdf(MultipartFile file, UUID personId) throws Exception {
        byte[] pdfBytes = file.getBytes();
        PersonDTO personDTO = extractPassportData(personId);
        String personData = String.valueOf(personDTO.hashCode());
        byte[] seed = deriveSeedFromPassport(personData);
        KeyPair keyPair = signGeneratorService.generateDeterministicECKeyPair(seed);
        X509Certificate certificate = signGeneratorService.generateSelfSignedCertificate(keyPair, personData);
        ByteArrayOutputStream signedPdf = new ByteArrayOutputStream();

        try (ByteArrayInputStream input = new ByteArrayInputStream(pdfBytes)) {
            RandomAccessReadBuffer randomAccess = new RandomAccessReadBuffer(input);
            PDDocument document = Loader.loadPDF(randomAccess);
            PDSignature signature = new PDSignature();
            signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
            signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
            signature.setName(personDTO.getPassport().getFullName());
            signature.setLocation("Educational Project");
            signature.setReason("Signed by user");
            signature.setSignDate(Calendar.getInstance());

            int pageNumber = document.getNumberOfPages() - 1;
            PDPage page = document.getPage(pageNumber);
            PDRectangle mediaBox = page.getMediaBox();

            float width = 200;
            float height = 50;
            float x = mediaBox.getWidth() - width - 10;
            float y = 10;

            BufferedImage emptyImage = new BufferedImage(200, 50, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = emptyImage.createGraphics();
            g.setPaint(Color.WHITE);
            g.fillRect(0, 0, emptyImage.getWidth(), emptyImage.getHeight());
            g.setPaint(Color.BLACK);
            g.drawString("Signed by " + personDTO.getPassport().getFullName(), 10, 25);
            g.dispose();

            ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
            ImageIO.write(emptyImage, "png", imgBytes);

            PDVisibleSignDesigner visibleSignDesigner = new PDVisibleSignDesigner(
                    document, new ByteArrayInputStream(imgBytes.toByteArray()), pageNumber);
            visibleSignDesigner.xAxis(x).yAxis(y).width(width).height(height);

            PDVisibleSigProperties visibleSigProperties = new PDVisibleSigProperties();

            visibleSigProperties.signerName(personDTO.getPassport().getFullName())
                    .signatureReason("Reason")
                    .signerLocation("Test location")
                    .preferredSize(0)
                    .visualSignEnabled(true)
                    .setPdVisibleSignature(visibleSignDesigner)
                    .page(pageNumber);

            SignatureOptions signatureOptions = new SignatureOptions();
            signatureOptions.setVisualSignature(visibleSigProperties);
            signatureOptions.setPage(pageNumber);

            document.addSignature(signature, new SignatureInterface() {
                @Override
                public byte[] sign(InputStream content) throws IOException {
                    try {
                        Signature signer = Signature.getInstance("SHA256withECDSA", "BC");
                        signer.initSign(keyPair.getPrivate());
                        byte[] buffer = content.readAllBytes();
                        signer.update(buffer);
                        return signer.sign();
                    } catch (Exception e) {
                        throw new IOException("Signing failed", e);
                    }
                }
            }, signatureOptions);

            document.saveIncremental(signedPdf);
            return signedPdf.toByteArray();
        }
    }


    private byte[] deriveSeedFromPassport(String passportData) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(passportData.getBytes(StandardCharsets.UTF_8));
    }

//    TODO use more relevant data
    private PersonDTO extractPassportData (UUID personId) {

        return personService.getPersonById(personId)
                .orElseThrow(() -> new NotFoundException("Person not found!"));
    }
}
