package peniaka.bankid.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import peniaka.bankid.annotation.ValidExpiryDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverLicenseDTO {
    private UUID id;
    @NotNull(message = "Category is mandatory!")
    private Set<DriverLicenseCategoryDTO> categories = new HashSet<>();
    @NotNull(message = "Issue date is mandatory!")
    private LocalDate issueDate;
    @ValidExpiryDate
    private LocalDate expirationDate;
    @NotBlank(message = "Authority code is mandatory!")
    @Size(min = 6, max = 6, message = "Must be 6 digit length!")
    private String issuedBy;
//    @UniqueDriverLicenseNumber
    @NotBlank(message = "Password ID is mandatory!")
    @Size(min = 9, max = 9, message = "Must be 9 digit length!")
    private String documentNumber;
}
