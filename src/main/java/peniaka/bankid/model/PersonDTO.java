package peniaka.bankid.model;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import peniaka.bankid.entity.DriverLicense;
import peniaka.bankid.entity.Passport;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
    private UUID id;
    @Valid
    @NotNull(message = "Passport credentials is required!")
    private PassportDTO passport;
    @Valid
    @NotNull(message = "Driver license credentials is required!")
    private DriverLicenseDTO driverLicense;
}
