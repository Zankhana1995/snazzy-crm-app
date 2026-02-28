package com.snazzy.crm.dto;

import com.snazzy.crm.validation.ValidAccountRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
@ValidAccountRequest
public class AccountRequest {

    @NotBlank(message = "Name is mandatory.")
    private String name;
    private String status;
    private Integer attempts;
    private List<ContactRequest> contacts;
}
