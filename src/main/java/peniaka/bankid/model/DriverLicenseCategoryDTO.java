package peniaka.bankid.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverLicenseCategoryDTO {
    private UUID id;
//    private DriverLicenseDTO license;
    private LicenseCategory category;
    private LocalDate issueDate;
}
