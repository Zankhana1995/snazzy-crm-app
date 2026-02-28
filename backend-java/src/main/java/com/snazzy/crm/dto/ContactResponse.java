package com.snazzy.crm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactResponse {

    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean primary;
}
