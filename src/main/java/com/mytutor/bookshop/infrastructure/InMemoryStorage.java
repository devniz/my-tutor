package com.mytutor.bookshop.infrastructure;

import com.mytutor.bookshop.domain.Book;

import java.math.BigDecimal;
import java.util.Map;

public interface InMemoryStorage {

    void createNewStore(BigDecimal budget);

    void updateBookStock(String bookType, Integer quantity);

    void restock(String bookType, Integer quantity);

    void cashIn(Integer quantity, BigDecimal price);

    void cashOut(BigDecimal restockPrice);

    void setCurrentStore(Map<String, Book> store);

    void setBudget(BigDecimal budget);

    BigDecimal getBudget();

    Map<String, Book> getCurrentStore();

}
