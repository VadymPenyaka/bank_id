package peniaka.bankid.util;

import org.springframework.stereotype.Service;
import peniaka.bankid.model.DriverLicenseCategoryDTO;
import peniaka.bankid.model.DriverLicenseDTO;

import java.time.LocalDate;
import java.util.*;

@Service
public class DriverLicenseGenerator {

    private final Random random = new Random();
    private final Set<DriverLicenseCategoryDTO> ALL_CATEGORIES = Set
            .of();

    public DriverLicenseDTO generateRandomDriverLicenseDTO() {

        DriverLicenseDTO dto = new DriverLicenseDTO();
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
        return issueDate.plusYears(10).minusDays(random.nextInt(365));
    }

    private String generateAuthorityCode() {
        return String.format("%06d", random.nextInt(1_000_000));
    }

    private String generateDriverLicenseNumber() {
        return String.format("%09d", random.nextInt(1_000_000_000));
    }
}
