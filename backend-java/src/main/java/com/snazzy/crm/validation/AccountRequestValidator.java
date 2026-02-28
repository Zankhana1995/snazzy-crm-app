package com.snazzy.crm.validation;

import com.snazzy.crm.dto.AccountRequest;
import com.snazzy.crm.model.AccountStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class AccountRequestValidator implements ConstraintValidator<ValidAccountRequest, AccountRequest> {

    private static final List<String> ALLOWED_STATUSES = List.of("NEW", "SOLD");

    @Override
    public boolean isValid(AccountRequest request, ConstraintValidatorContext context) {

        if (request == null) {
            return true;
        }

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        if (request.getStatus() == null || request.getStatus().isBlank()) {
            context.buildConstraintViolationWithTemplate("Status is mandatory.")
                    .addPropertyNode("status")
                    .addConstraintViolation();
            return false;
        }

        String normalizedStatus = request.getStatus().toUpperCase();

        if (!ALLOWED_STATUSES.contains(normalizedStatus)) {
            context.buildConstraintViolationWithTemplate("Status must be NEW or SOLD.")
                    .addPropertyNode("status")
                    .addConstraintViolation();
            valid = false;
        }

        if (AccountStatus.SOLD.name().equals(normalizedStatus)) {
            if (request.getAttempts() == null || request.getAttempts() < 1) {
                context.buildConstraintViolationWithTemplate(
                                "Attempts must be 1 or greater when status is SOLD.")
                        .addPropertyNode("attempts")
                        .addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }
}
