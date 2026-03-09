package com.snazzy.crm.repository;

import com.snazzy.crm.dto.AccountSearch;
import com.snazzy.crm.model.Account;
import com.snazzy.crm.search.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account>, SearchRepository<Account, AccountSearch> {

    @EntityGraph(attributePaths = "contacts")
    Page<Account> findAll(Specification<Account> spec, Pageable pageable);

}