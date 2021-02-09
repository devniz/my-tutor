package com.mytutor.bookshop.infrastructure;

import com.mytutor.bookshop.domain.Book;
import com.mytutor.bookshop.infrastructure.exception.OutOfStockException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InMemoryStorageImplSmallTest {

    @Mock
    private static ReportImpl mockReport;

    @BeforeAll
    public static void setUp() {
        mockReport = new ReportImpl();
    }

    @Test
    @DisplayName("Verify creation of a new book store")
    void itShouldCreateNewStoreWithInitialBudgetAndBooks() {
        InMemoryStorageImpl storage = new InMemoryStorageImpl(mockReport);

        storage.createNewStore(new BigDecimal("500.00"));
        assertEquals(createNewStore(), storage.getCurrentStore());
    }

    @Test
    @DisplayName("Verify update book stock")
    void itShouldUpdateCurrentStockGivenQuantityAndBookType() {
        InMemoryStorageImpl storage = new InMemoryStorageImpl(new ReportImpl());

        storage.createNewStore(new BigDecimal("500.00"));
        storage.updateBookStock("C", 1);

        assertEquals(9, storage.getCurrentStore().get("C").getQuantity());
    }

    @Test
    @DisplayName("Verify out of stock")
    void itShouldThrowOutOfStockException() {
        InMemoryStorageImpl storage = new InMemoryStorageImpl(mockReport);
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
        InMemoryStorageImpl storage = new InMemoryStorageImpl(mockReport);

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

    @Test
    @DisplayName("Verify cash out logic")
    void itShouldUpdateBudgetWhenRestock() {
        InMemoryStorageImpl storage = new InMemoryStorageImpl(mockReport);

        storage.createNewStore(new BigDecimal("500.00"));
        storage.cashOut(new BigDecimal("25.00"));

        assertEquals(new BigDecimal("325.00"), storage.getBudget());
    }

    @Test
    @DisplayName("Verify cash in logic")
    void itShouldUpdateBudgetWhenSaleOccur() {
        InMemoryStorageImpl storage = new InMemoryStorageImpl(mockReport);

        storage.createNewStore(new BigDecimal("500.00"));
        storage.cashIn(2, new BigDecimal("30.00"));

        assertEquals(new BigDecimal("560.00"), storage.getBudget());
    }

    @Test
    @DisplayName("Verify concurrent purchases")
    void itShouldUpdateDataWithConcurrentPurchasesAndAvoidRaceCondition() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(2);
        InMemoryStorageImpl s = new InMemoryStorageImpl(mockReport);

        s.createNewStore(new BigDecimal("500.00"));

        Callable<Void> purchase = () -> {
            latch.countDown();
            latch.await();
            s.updateBookStock("D", 1);
            return null;
        };

        Future<Void> t1 = executor.submit(purchase);
        Future<Void> t2 = executor.submit(purchase);

        t1.get();
        t2.get();
        executor.shutdown();

        assertEquals(new BigDecimal("560.00"), s.getBudget());
        assertEquals(8, s.getCurrentStore().get("D").getQuantity());
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