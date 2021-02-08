package com.mytutor.bookshop.infrastructure;

import com.mytutor.bookshop.domain.Book;

import java.math.BigDecimal;
import java.util.Map;

public interface InMemoryStorage {

    void createNewStore(BigDecimal budget);

    void updateBookStock(String bookType, Integer quantity);


    void restock(String bookType, Integer quantity);

    void setCurrentStore(Map<String, Book> store);

    Map<String, Book> getCurrentStore();

}
