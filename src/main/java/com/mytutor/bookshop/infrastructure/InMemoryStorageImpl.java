package com.mytutor.bookshop.infrastructure;

import com.mytutor.bookshop.domain.Book;
import com.mytutor.bookshop.infrastructure.exception.OutOfStockException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
public class InMemoryStorageImpl implements InMemoryStorage {

    private static final Integer _MIN_STOCK_ = 3;

    BigDecimal initialBudget;
    Map<String, Book> store;

    @Override
    public void createNewStore(BigDecimal budget) {
        this.initialBudget = budget;
        Map<String, Book> store = new HashMap<>() {
            {
                put("A", new Book(new BigDecimal("25.00"), 10));
                put("B", new Book(new BigDecimal("20.00"), 10));
                put("C", new Book(new BigDecimal("23.00"), 10));
                put("D", new Book(new BigDecimal("30.00"), 10));
                put("E", new Book(new BigDecimal("27.00"), 10));
            }
        };
        this.setCurrentStore(store);
    }

    @Override
    public void updateBookStock(String bookType, Integer quantity) {
        Supplier<Stream<Map<String, Book>>> streamSupplier = () -> Stream.of(this.store);

        Optional<Integer> bookToOrderQuantity = streamSupplier
                .get()
                .map(q -> q.get(bookType).getQuantity())
                .findFirst();

        if (bookToOrderQuantity.isPresent() && bookToOrderQuantity.get() >= quantity) {
            var orderedBookPrice = streamSupplier
                    .get()
                    .map(t -> t.get(bookType))
                    .findFirst()
                    .get()
                    .getPrice();

            var orderedBookNewQuantity = streamSupplier
                    .get()
                    .map(t -> t.get(bookType))
                    .peek(q -> q.setQuantity(q.getQuantity() - quantity))
                    .findFirst()
                    .get()
                    .getQuantity();

            this.store.put(bookType, new Book(
                    new BigDecimal(String.valueOf(orderedBookPrice)),
                    orderedBookNewQuantity
            ));
        } else {
            this.restock(bookType, bookToOrderQuantity.get());
            throw new OutOfStockException("Book " + bookType + " is out of stock.");
        }
    }

    @Override
    public void restock(String bookType, Integer quantity) {
        if (quantity < _MIN_STOCK_) {
            Supplier<Stream<Map<String, Book>>> streamSupplier = () -> Stream.of(this.store);

            var bookToRestockPrice = streamSupplier
                    .get()
                    .map(t -> t.get(bookType))
                    .findFirst()
                    .get()
                    .getPrice();

            var bookToRestockNewQuantity = streamSupplier
                    .get()
                    .map(t -> t.get(bookType))
                    .peek(q -> q.setQuantity(q.getQuantity() + 10))
                    .findFirst()
                    .get()
                    .getQuantity();
            this.store.put(bookType, new Book(
                    new BigDecimal(String.valueOf(bookToRestockPrice)),
                    bookToRestockNewQuantity
            ));
        }
    }

    @Override
    public void setCurrentStore(Map<String, Book> store) {
        this.store = store;
    }

    @Override
    public Map<String, Book> getCurrentStore() {
        return this.store;
    }
}
