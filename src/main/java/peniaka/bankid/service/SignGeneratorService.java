package peniaka.bankid.service;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Date;
import javax.security.auth.x500.X500Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class SignGeneratorService {

    public KeyPair generateDeterministicECKeyPair(byte[] seed) throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(seed);

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "BC");
        keyGen.initialize(256, random);

        return keyGen.generateKeyPair();
    }

    public X509Certificate generateSelfSignedCertificate(KeyPair keyPair, String passportData) throws Exception {
        ZonedDateTime start = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime end = start.plusYears(1);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(passportData.getBytes(StandardCharsets.UTF_8));
        BigInteger serialNumber = new BigInteger(1, hash);

        String cn = passportData.length() > 64 ? passportData.substring(0, 64) : passportData;
        X500Principal subject = new X500Principal("CN=" + cn);

        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                subject,
                serialNumber,
                Date.from(start.toInstant()),
                Date.from(end.toInstant()),
                subject,
                keyPair.getPublic()
        );

        ContentSigner signer = new JcaContentSignerBuilder("SHA256withECDSA")
                .setProvider("BC")
                .build(keyPair.getPrivate());

        X509CertificateHolder certHolder = certBuilder.build(signer);
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
    }

}
