package com.snazzy.crm.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccountResponse {

    private Integer id;
    private String name;
    private String status;
    private Integer attempts;
    private List<ContactResponse> contacts;
}
