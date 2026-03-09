package com.snazzy.crm.controller;

import com.snazzy.crm.dto.AccountRequest;
import com.snazzy.crm.dto.AccountResponse;
import com.snazzy.crm.dto.AccountSearch;
import com.snazzy.crm.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/account")
    public List<AccountResponse> search(final AccountSearch accountSearch) {
        return accountService.search(accountSearch);
    }

    @GetMapping("/accountwithPage")
    public Page<AccountResponse> searchWithPage(AccountSearch search, Pageable pageable) {
        return accountService.searchWithPage(search, pageable);
    }

    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create(@Valid @RequestBody AccountRequest request) {
        return accountService.create(request);
    }
}
