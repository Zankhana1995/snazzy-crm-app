package com.snazzy.crm.controller;

import com.snazzy.crm.dto.AccountSearch;
import com.snazzy.crm.model.Account;
import com.snazzy.crm.repository.AccountRepository;
import com.snazzy.crm.service.SearchService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;

    private final SearchService searchService;

    @GetMapping("/account")
    public List<Account> search(final AccountSearch accountSearch) {
        return this.searchService.search(accountSearch, this.accountRepository);
    }

    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@Valid @RequestBody Account account) {
        return this.accountRepository.save(account);
    }
}
