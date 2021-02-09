package com.mytutor.bookshop.infrastructure;

import com.mytutor.bookshop.domain.Book;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentMap;

public interface InMemoryStorage {

    void createNewStore(BigDecimal budget);

    void updateBookStock(String bookType, Integer quantity);

    void restock(String bookType, Integer quantity);

    void cashIn(Integer quantity, BigDecimal price);

    void cashOut(BigDecimal restockPrice);

    void setCurrentStore(ConcurrentMap<String, Book> store);

    void setBudget(BigDecimal budget);

    BigDecimal getBudget();

    ConcurrentMap<String, Book> getCurrentStore();

}
