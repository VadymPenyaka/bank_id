package peniaka.bankid.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import peniaka.bankid.validation.BirthDateValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthDateValidator.class)
public @interface ValidBirthDate {
    String message() default "You must be 18 or older!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
