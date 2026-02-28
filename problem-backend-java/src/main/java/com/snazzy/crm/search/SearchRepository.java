package com.snazzy.crm.search;

import java.util.List;

public interface SearchRepository<T, S extends Search> {
    List<T> search(S search);
}
