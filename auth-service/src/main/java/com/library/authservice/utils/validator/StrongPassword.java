package com.library.authservice.utils.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrongPassword {
    String message() default "Ít nhất 8 kí tự (chữ thường, hoa, số, kí tự đặc biệt)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
