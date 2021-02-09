package com.mytutor.bookshop.rest.v1.service;

import com.mytutor.bookshop.infrastructure.exception.QuantityExceedsStockException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookshopServiceSmallTest {

    private static BookshopService service;

    @BeforeAll
    public static void setUp() {
        service = new BookshopService();
    }

    @Test
    @DisplayName("Verify exceeds maximum stock")
    public void itShouldThrowQuantityExceedsStockExceptionWhenQuantityIsBiggerThanMaxStock() {
        try {
            service.orderBook("A", 100);
        } catch (QuantityExceedsStockException ex) {
            assertEquals(QuantityExceedsStockException.class, ex.getClass());
            assertEquals("the maximum quantity you can order is 10.", ex.getMessage());
        }
    }
}