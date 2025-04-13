package peniaka.bankid.generator;

import org.springframework.stereotype.Service;
import peniaka.bankid.model.DriverLicenseDTO;

import java.time.LocalDate;
import java.util.*;

@Service
public class DriverLicenseGenerator {

    private final Random random = new Random();
    private final Set<String> ALL_CATEGORIES = Set.of("A", "B", "C", "A1", "B1", "C1");

    public DriverLicenseDTO generateRandomDriverLicenseDTO() {
        DriverLicenseDTO dto = new DriverLicenseDTO();
        dto.setId(UUID.randomUUID());
        dto.setCategory(ALL_CATEGORIES);
        dto.setIssueDate(generateIssueDate());
        dto.setExpirationDate(generateExpirationDate(dto.getIssueDate()));
        dto.setIssuedBy(generateAuthorityCode());
        dto.setDocumentNumber(generateDriverLicenseNumber());
        return dto;
    }

    private LocalDate generateIssueDate() {
        int yearsAgo = 1 + random.nextInt(20);
        return LocalDate.now().minusYears(yearsAgo).minusDays(random.nextInt(365));
    }

    private LocalDate generateExpirationDate(LocalDate issueDate) {
        // Видаються терміном на 10 років
        return issueDate.plusYears(10).minusDays(random.nextInt(365));
    }

    private String generateAuthorityCode() {
        return String.format("%06d", random.nextInt(1_000_000));
    }

    private String generateDriverLicenseNumber() {
        return String.format("%09d", random.nextInt(1_000_000_000));
    }
}
