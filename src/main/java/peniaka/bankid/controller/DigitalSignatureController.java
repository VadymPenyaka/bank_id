package peniaka.bankid.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import peniaka.bankid.service.DigitalSignatureService;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping(DigitalSignatureController.BASE_PATH)
@RequiredArgsConstructor
public class DigitalSignatureController {
    public static final String BASE_PATH = "/api/v1/sign";
    private final DigitalSignatureService digitalSignatureService;

    @PostMapping
    public ResponseEntity<byte[]> signPdf( @RequestParam("file") MultipartFile file, @RequestParam("personId") UUID personId) {
        try {
            byte[] signedPdf = digitalSignatureService.signPdf(file, personId);
            FileOutputStream bf = new FileOutputStream(new File("save111.pdf"));
            bf.write(signedPdf);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("signed_document.pdf")
                    .build());

            return new ResponseEntity<>(signedPdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Signing failed: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }
}
