package peniaka.bankid.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Converter
@Component
public class SensitiveDataConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String s) {
        if (s == null) {
            return null;
        }
        try {
            return SensitiveDataCipher.encrypt(s);
        } catch (Exception e) {
            throw new IllegalStateException("Error encrypting sensitive data", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        try {
            return SensitiveDataCipher.decrypt(s);
        } catch (Exception e) {
            throw new IllegalStateException("Error encrypting sensitive data", e);
        }
    }
}
