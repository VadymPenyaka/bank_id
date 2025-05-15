package peniaka.bankid.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import peniaka.bankid.validation.UniquePassportNumberValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy =  UniquePassportNumberValidator.class)
public @interface UniquePassportNumber {
    String message() default "Passport number is already registered!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
