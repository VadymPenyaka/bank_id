package peniaka.bankid.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverLicenseDTO {
    private UUID id;
    private LocalDate issueDate;
    private LocalDate expirationDate;
    private String issuedBy;
    private String documentNumber;
    private Set<DriverLicenseCategoryDTO> category;
}
