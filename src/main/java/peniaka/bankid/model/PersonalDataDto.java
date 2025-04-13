package peniaka.bankid.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDataDto {
    private PassportDTO passport;
    private DriverLicenseDTO driverLicense;
}
