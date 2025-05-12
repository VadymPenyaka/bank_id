package peniaka.bankid.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import peniaka.bankid.annotation.ValidBirthDate;
import peniaka.bankid.annotation.ValidExpiryDate;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassportDTO {
    private UUID id;
    @ValidBirthDate
    private LocalDate dateOfBirth;
//    @UniquePassportNumber
    @NotBlank(message = "Password ID is mandatory!")
    @Size(min = 9, max = 9, message = "Must be 9 digit length!")
    private String documentNumber;
    @NotBlank(message = "Authority code is mandatory!")
    @Size(min = 6, max = 6, message = "Must be 6 digit length!")
    private String issuedBy;
    @ValidExpiryDate
    private LocalDate expirationDate;
    @NotBlank(message = "Tax identification number is mandatory!")
    @Size(min = 10, max = 10, message = "Must be 10 digit length!")
    private String taxIdentificationNumber;
}
