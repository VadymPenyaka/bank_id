package peniaka.bankid.util;

import org.springframework.stereotype.Service;
import peniaka.bankid.model.PassportDTO;

import java.time.LocalDate;
import java.time.Period;
import java.util.Random;

@Service
public class PassportGenerator {

    private final Random random = new Random();

    public PassportDTO generateRandomPassportDTO() {
        PassportDTO dto = new PassportDTO();
        dto.setDateOfBirth(generateValidBirthDate());
        dto.setDocumentNumber(generatePassportNumber());
        dto.setIssuedBy(generateAuthorityCode());
        dto.setExpirationDate(generateValidExpirationDate(dto.getDateOfBirth()));
        dto.setTaxIdentificationNumber(generateTaxIdentificationNumber());
        return dto;
    }

    private LocalDate generateValidBirthDate() {
        int age = 21 + random.nextInt(47);
        return LocalDate.now().minusYears(age)
                .minusDays(random.nextInt(365));
    }

    private String generatePassportNumber() {
        return String.format("%09d", random.nextInt(1_000_000_000));
    }

    private String generateAuthorityCode() {
        return String.format("%06d", random.nextInt(1_000_000));
    }

    private LocalDate generateValidExpirationDate(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        LocalDate minValidDate = today.plusYears(5);
        LocalDate maxValidDate = today.plusYears(15);
        long daysRange = Period.between(minValidDate, maxValidDate).getDays();
        return minValidDate.plusDays(random.nextInt((int) Math.max(daysRange, 1)));
    }

    private String generateTaxIdentificationNumber() {
        StringBuilder tin = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            tin.append(random.nextInt(10));
        }
        tin.append(random.nextInt(10));
        return tin.toString();
    }
}