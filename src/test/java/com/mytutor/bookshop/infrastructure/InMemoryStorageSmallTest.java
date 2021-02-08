package com.mytutor.bookshop.infrastructure;

import com.mytutor.bookshop.domain.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InMemoryStorageSmallTest {

    @InjectMocks
    private InMemoryStorage inMemoryStorage;

    @Test
    @DisplayName("Verify creation of a new book store")
    void itShouldCreateNewStoreWithInitialBudgetAndBooks() {
        inMemoryStorage.createNewStore(new BigDecimal("500.00"));
        assertEquals(inMemoryStorage.initialBudget, new BigDecimal("500.00"));
        assertEquals(inMemoryStorage.store, createNewStore());
    }

    @Test
    @DisplayName("Verify update initial budget")
    void itShouldUpdateInitialBudgetWithNewAmount() {
        inMemoryStorage.createNewStore(new BigDecimal("500.00"));
        inMemoryStorage.updateInitialBudget(new BigDecimal("10.00"));
        assertEquals(inMemoryStorage.initialBudget, new BigDecimal("490.00"));
    }

    @Test
    @DisplayName("Verify update book stock")
    void itShouldUpdateCurrentStockBasedOnSalesQuantityAndBookType() {
        inMemoryStorage.createNewStore(new BigDecimal("500.00"));
        inMemoryStorage.updateBookStock("C", 1);
        var orderedBook = inMemoryStorage.store
                .entrySet()
                .stream()
                .filter(type -> type.getKey().equals("C"))
                .map(Map.Entry::getValue);
        assertEquals(9, orderedBook.findFirst().get().getQuantity());
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