package com.snazzy.crm.service;

import com.snazzy.crm.dto.*;
import com.snazzy.crm.model.Account;
import com.snazzy.crm.model.AccountStatus;
import com.snazzy.crm.model.Contact;
import com.snazzy.crm.repository.AccountRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<AccountResponse> search(AccountSearch search) {
        return accountRepository.search(search)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Page<AccountResponse> searchWithPage(AccountSearch search, Pageable pageable) {
        Specification<Account> spec = buildSpec(search);
        Page<Account> page = accountRepository.findAll(spec, pageable);
        return page.map(this::toResponse);
    }

    public Specification<Account> buildSpec(AccountSearch search) {
        return (root, query, cb) -> {

            Join<Account, Contact> contactJoin = root.join("contacts", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (search.getQuery() != null && !search.getQuery().isBlank()) {
                predicates.add(cb.like(contactJoin.get("phoneNumber"), "%" + search.getQuery() + "%")
                );
            }

            if (Boolean.TRUE.equals(search.getUnassigned())) {
                predicates.add(cb.isNull(contactJoin.get("id")));
            }

            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public AccountResponse create(AccountRequest request) {

        Account account = new Account();
        account.setName(request.getName());
        account.setAttempts(
                java.util.Optional.ofNullable(request.getAttempts()).orElse(0)
        );
        account.setStatus(AccountStatus.valueOf(request.getStatus().toUpperCase()));

        if (request.getContacts() != null) {
            request.getContacts().forEach(contactRequest -> {
                Contact contact = new Contact();
                contact.setFirstName(contactRequest.getFirstName());
                contact.setLastName(contactRequest.getLastName());
                contact.setPhoneNumber(contactRequest.getPhoneNumber());
                contact.setPrimary(contactRequest.getPrimary() != null
                        ? contactRequest.getPrimary()
                        : false);
                contact.setAccount(account);
                account.getContacts().add(contact);
            });
        }

        Account saved = accountRepository.save(account);

        return toResponse(saved);
    }

    private AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .status(account.getStatus() != null ? account.getStatus().name() : null)
                .attempts(account.getAttempts())
                .contacts(account.getContacts() != null
                        ? account.getContacts().stream()
                        .map(contact -> ContactResponse.builder()
                                .id(contact.getId())
                                .firstName(contact.getFirstName())
                                .lastName(contact.getLastName())
                                .phoneNumber(contact.getPhoneNumber())
                                .primary(contact.getPrimary())
                                .build())
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}
