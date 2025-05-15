package peniaka.bankid.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import peniaka.bankid.annotation.UniqueDriverLicenseNumber;
import peniaka.bankid.service.DriverLicenseService;

@RequiredArgsConstructor
public class UniqueLicenseNumberValidator implements ConstraintValidator <UniqueDriverLicenseNumber, String> {
    private final DriverLicenseService driverLicenseService;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !driverLicenseService.existsByDocumentNumber(s);
    }
}
