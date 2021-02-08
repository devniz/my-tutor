package com.mytutor.bookshop.infrastructure;

import com.mytutor.bookshop.domain.Book;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class InMemoryStorage {

    BigDecimal initialBudget;
    Map<String, Book> store;

    public void createNewStore(BigDecimal initialBudget) {
        this.initialBudget = initialBudget;
        this.store = new HashMap<>();
        this.store.put("A", new Book(new BigDecimal("25.00"), 10));
        this.store.put("B", new Book(new BigDecimal("20.00"), 10));
        this.store.put("C", new Book(new BigDecimal("23.00"), 10));
        this.store.put("D", new Book(new BigDecimal("30.00"), 10));
        this.store.put("E", new Book(new BigDecimal("27.00"), 10));
    }

    public void updateInitialBudget(BigDecimal amount) {
        this.initialBudget = initialBudget.subtract(amount);
    }

    public void updateBookStock(String bookType, Integer quantity) {
        var bookToOrder = this.store
                .entrySet()
                .stream()
                .filter(type -> type.getKey().equals(bookType))
                .peek(b -> b.getValue().setQuantity(b.getValue().getQuantity() - quantity))
                .map(Map.Entry::getValue);
        this.store.put(bookType, bookToOrder.findFirst().get());
    }

}
