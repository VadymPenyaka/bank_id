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
public class PassportDTO {
    private UUID id;
    private String fullName;
    private LocalDate dateOfBirth;
    private String documentNumber;
    private String issuedBy;
    private LocalDate expirationDate;
    private String taxIdentificationNumber;
}
