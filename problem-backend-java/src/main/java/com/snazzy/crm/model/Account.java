package com.snazzy.crm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "account")
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is mandatory.")
    private String name;

    @Column(name = "status")
    @NotBlank(message = "Status is mandatory.")
    private String status;

    @Column(name = "attempts", nullable = false)
    private Integer attempts = 0;

    @Transient
    private List<Contact> contacts;

}