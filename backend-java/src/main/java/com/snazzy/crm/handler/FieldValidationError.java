package com.snazzy.crm.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FieldValidationError {

    private String field;
    private String message;
}
