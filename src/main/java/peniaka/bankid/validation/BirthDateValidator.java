package peniaka.bankid.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import peniaka.bankid.annotation.ValidBirthDate;

import java.time.LocalDate;

public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
        return LocalDate.now().minusYears(birthDate.getYear()).getYear() >= 18;
    }
}
