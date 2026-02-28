package com.snazzy.crm.repository;

import com.snazzy.crm.dto.AccountSearch;
import com.snazzy.crm.model.Account;
import com.snazzy.crm.model.Contact;
import com.snazzy.crm.search.SearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class AccountRepositoryImpl implements SearchRepository<Account, AccountSearch> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Account> search(AccountSearch search) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = cb.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);

        root.fetch("contacts", JoinType.LEFT);

        Join<Account, Contact> contactJoin = root.join("contacts", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (search.getQuery() != null && !search.getQuery().isBlank()) {
            String likePattern = "%" + search.getQuery() + "%";
            predicates.add(
                    cb.like(contactJoin.get("phoneNumber"), likePattern)
            );
        }

        if (Boolean.TRUE.equals(search.getUnassigned())) {
            predicates.add(
                    cb.isNull(contactJoin.get("id"))
            );
        }

        query.select(root).distinct(true);

        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        query.orderBy(cb.asc(root.get("id")));

        return entityManager.createQuery(query).getResultList();
    }
}

/**

 SELECT DISTINCT a.*
 FROM account a
 LEFT JOIN contact c ON c.account_id = a.id
 WHERE (c.phone_number LIKE '%query%')
 AND (c.id IS NULL)
 ORDER BY a.id ASC;

 */