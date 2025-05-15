package peniaka.bankid.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import peniaka.bankid.annotation.UniquePassportNumber;
import peniaka.bankid.service.PassportService;

@RequiredArgsConstructor
public class UniquePassportNumberValidator implements ConstraintValidator<UniquePassportNumber, String> {
    private final PassportService passportService;
    @Override
    public boolean isValid(String passportNumber, ConstraintValidatorContext constraintValidatorContext) {
        return !passportService.existsByDocumentNumber(passportNumber);
    }
}
