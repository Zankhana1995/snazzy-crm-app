package com.snazzy.crm.repository;

import com.snazzy.crm.dto.AccountSearch;
import com.snazzy.crm.model.Account;
import com.snazzy.crm.search.SearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer>, SearchRepository<Account, AccountSearch> {
}