package peniaka.bankid.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import peniaka.bankid.annotation.ValidExpiryDate;

import java.time.LocalDate;

public class ExpiryDateValidator implements ConstraintValidator<ValidExpiryDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate expiryDate, ConstraintValidatorContext constraintValidatorContext) {
        return expiryDate.isAfter(LocalDate.now());
    }
}
