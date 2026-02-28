package com.snazzy.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snazzy.crm.CrmApplication;
import com.snazzy.crm.model.Account;
import com.snazzy.crm.model.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {CrmApplication.class})
public class AccountControllerTest {
    private MockMvc mvc;

    @Autowired
    private AccountController accountController;

    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<List<Account>> jsonAccounts;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(accountController)
                .build();
    }

    @Test
    @Transactional
    public void canList() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/account"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        System.out.println(response.getContentAsString());

        assertThat(response.getContentAsString()).isEqualTo(
                jsonAccounts.write(Arrays.asList(
                        Account.builder().id(1).name("Good Burger").status("NEW").attempts(0).contacts(Arrays.asList(
                                Contact.builder().id(2).firstName("Salim").lastName("Traher").phoneNumber("456-103-4512").primary(true).build(),
                                Contact.builder().id(3).firstName("Ewan").lastName("Maleck").phoneNumber("388-868-5602").primary(false).build()
                        )).build(),
                        Account.builder().id(2).name("Acme Corp").status("NEW").attempts(0).contacts(Arrays.asList(
                                Contact.builder().id(7).firstName("Saccount_idoney").lastName("Marconi").phoneNumber("137-718-5089").primary(true).build(),
                                Contact.builder().id(8).firstName("Hedda").lastName("Frie").phoneNumber("181-482-8234").primary(false).build()
                        )).build(),
                        Account.builder().id(3).name("Wonka Candy Company").status("NEW").attempts(0).contacts(Arrays.asList(
                                Contact.builder().id(12).firstName("Freddi").lastName("Weippert").phoneNumber("588-683-6350").primary(true).build(),
                                Contact.builder().id(13).firstName("Burr").lastName("Margrett").phoneNumber("993-823-2388").primary(false).build()
                        )).build(),
                        Account.builder().id(4).name("Great Glass Elevator Corp").status("NEW").attempts(0).contacts(new ArrayList<>()).build()
                )).getJson()
        );
    }

    @Test
    @Transactional
    public void canListWithQuery() throws Exception {
        MockHttpServletResponse response = mvc.perform(get("/account?query=388"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        System.out.println(response.getContentAsString());

        assertThat(response.getContentAsString()).isEqualTo(
                jsonAccounts.write(Arrays.asList(
                        Account.builder().id(1).name("Good Burger").status("NEW").attempts(0).contacts(Arrays.asList(
                                Contact.builder().id(2).firstName("Salim").lastName("Traher").phoneNumber("456-103-4512").primary(true).build(),
                                Contact.builder().id(3).firstName("Ewan").lastName("Maleck").phoneNumber("388-868-5602").primary(false).build()
                        )).build(),
                        Account.builder().id(3).name("Wonka Candy Company").status("NEW").attempts(0).contacts(Arrays.asList(
                                Contact.builder().id(12).firstName("Freddi").lastName("Weippert").phoneNumber("588-683-6350").primary(true).build(),
                                Contact.builder().id(13).firstName("Burr").lastName("Margrett").phoneNumber("993-823-2388").primary(false).build()
                        )).build()
                )).getJson()
        );
    }

    @Test
    @Transactional
    public void canListWithFilter() throws Exception {
        MockHttpServletResponse response1 = mvc.perform(get("/account?unassigned=true"))
                .andReturn().getResponse();

        assertThat(response1.getStatus()).isEqualTo(HttpStatus.OK.value());
        System.out.println(response1.getContentAsString());

        assertThat(response1.getContentAsString()).isEqualTo(
                jsonAccounts.write(Collections.singletonList(
                        Account.builder().id(4).name("Great Glass Elevator Corp").status("NEW").attempts(0).contacts(new ArrayList<>()).build()
                )).getJson()
        );

        MockHttpServletResponse response2 = mvc.perform(get("/account?unassigned=true&query=388"))
                .andReturn().getResponse();

        assertThat(response2.getStatus()).isEqualTo(HttpStatus.OK.value());
        System.out.println(response2.getContentAsString());

        assertThat(response2.getContentAsString()).isEqualTo(
                jsonAccounts.write(new ArrayList<>()).getJson()
        );
    }

    @Test
    @Transactional
    public void createRequiresValidStatus() throws Exception {
        Account account = new Account();
        account.setName("Box Factory");
        account.setStatus("SOLD");

        mvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(account).getBytes(StandardCharsets.UTF_8))
        ).andExpect(status().isBadRequest());

        account.setAttempts(1);

        mvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(account).getBytes(StandardCharsets.UTF_8))
        ).andExpect(status().isCreated());
    }
}
