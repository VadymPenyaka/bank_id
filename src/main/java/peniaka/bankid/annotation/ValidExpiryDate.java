package peniaka.bankid.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import peniaka.bankid.validation.ExpiryDateValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy =  ExpiryDateValidator.class)
public @interface ValidExpiryDate {
    String message() default "Your passport is expired.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
