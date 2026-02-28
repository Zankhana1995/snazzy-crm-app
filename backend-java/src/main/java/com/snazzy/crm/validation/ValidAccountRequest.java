package com.snazzy.crm.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AccountRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAccountRequest {

    String message() default "Invalid account request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
