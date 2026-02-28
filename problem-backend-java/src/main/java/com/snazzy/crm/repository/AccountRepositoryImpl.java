package com.snazzy.crm.repository;

import com.snazzy.crm.dto.AccountSearch;
import com.snazzy.crm.model.Account;
import com.snazzy.crm.search.SearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class AccountRepositoryImpl implements SearchRepository<Account, AccountSearch> {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Account> search(AccountSearch search) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = cb.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);

        query.select(root);
        query.orderBy(cb.asc(root.get("id")));

        return entityManager.createQuery(query).getResultList();
    }
}
