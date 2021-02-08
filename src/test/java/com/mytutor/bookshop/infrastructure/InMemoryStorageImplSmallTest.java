package com.mytutor.bookshop.infrastructure;

import com.mytutor.bookshop.domain.Book;
import com.mytutor.bookshop.infrastructure.exception.OutOfStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryStorageImplSmallTest {

    @Test
    @DisplayName("Verify creation of a new book store")
    void itShouldCreateNewStoreWithInitialBudgetAndBooks() {
        InMemoryStorageImpl storage = new InMemoryStorageImpl();

        storage.createNewStore(new BigDecimal("500.00"));
        assertEquals(createNewStore(), storage.getCurrentStore());
    }

    @Test
    @DisplayName("Verify update book stock")
    void itShouldUpdateCurrentStockGivenQuantityAndBookType() {
        InMemoryStorageImpl storage = new InMemoryStorageImpl();

        storage.createNewStore(new BigDecimal("500.00"));
        storage.updateBookStock("C", 1);

        assertEquals(9, storage.getCurrentStore().get("C").getQuantity());
    }

    @Test
    @DisplayName("Verify out of stock")
    void itShouldThrowOutOfStockException() {
        InMemoryStorageImpl storage = new InMemoryStorageImpl();
         storage.createNewStore(new BigDecimal("500.00"));

        storage.updateBookStock("A", 10);

        try {
            storage.updateBookStock("A", 1);
        } catch (OutOfStockException ex) {
            assertEquals(OutOfStockException.class, ex.getClass());
            assertEquals("Book A is out of stock.", ex.getMessage());
        }
    }

    @Test
    @DisplayName("Verify restock logic")
    void itShouldRestockIfQuantityIsLessThanThreeItemsAndUpdateInitialBudget() {
        InMemoryStorageImpl storage = new InMemoryStorageImpl();

        storage.createNewStore(new BigDecimal("500.00"));
        storage.updateBookStock("D", 8);

        try {
            storage.updateBookStock("D", 5);
        } catch (OutOfStockException ex) {
            var currentStore = storage.getCurrentStore();
            assertEquals(12, currentStore.get("D").getQuantity());
            assertEquals(new BigDecimal("530.00"), storage.getBudget());
        }
    }

    private HashMap<String, Book> createNewStore() {
        return new HashMap<>() {
            {
                put("A", new Book(new BigDecimal("25.00"), 10));
                put("B", new Book(new BigDecimal("20.00"), 10));
                put("C", new Book(new BigDecimal("23.00"), 10));
                put("D", new Book(new BigDecimal("30.00"), 10));
                put("E", new Book(new BigDecimal("27.00"), 10));
            }
        };
    }

}