package com.snazzy.crm.dto;

import lombok.Data;

@Data
public class ContactRequest {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean primary;
}
