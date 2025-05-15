package peniaka.bankid.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFullInfoRequest {
    @Valid
    @NotNull(message = "Passport credentials is required!")
    private PassportDTO passport;
    @Valid
    @NotNull(message = "Driver license credentials is required!")
    private DriverLicenseDTO driverLicense;
}
