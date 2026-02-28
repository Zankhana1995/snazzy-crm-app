package com.snazzy.crm.handler;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApiErrorResponse {

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldValidationError> errors;
}
