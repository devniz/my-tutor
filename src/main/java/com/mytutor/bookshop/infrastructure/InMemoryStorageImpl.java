package com.mytutor.bookshop.infrastructure;

import com.mytutor.bookshop.domain.Book;
import com.mytutor.bookshop.infrastructure.exception.OutOfStockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
@Component
public class InMemoryStorageImpl implements InMemoryStorage {

    private static final Integer _MIN_STOCK_ = 3;
    private static final BigDecimal _SUPPLIER_RATE_ = new BigDecimal("0.7");
    private final ReportImpl report;

    @Autowired
    public InMemoryStorageImpl(ReportImpl report) {
        this.report = report;
    }

    BigDecimal budget;
    ConcurrentMap<String, Book> store;

    @Override
    public void createNewStore(BigDecimal budget) {
        this.setBudget(budget);
        ConcurrentMap<String, Book> store = new ConcurrentHashMap<>() {
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
    synchronized public void updateBookStock(String bookType, Integer quantity) {
        Supplier<Stream<ConcurrentMap<String, Book>>> streamSupplier = () -> Stream.of(this.store);

        Optional<Integer> bookToOrderQuantity = streamSupplier
                .get()
                .map(q -> q.get(bookType).getQuantity())
                .findFirst();

        Optional<BigDecimal> bookToOrderPrice = streamSupplier
                .get()
                .map(p -> p.get(bookType).getPrice())
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

            this.cashIn(quantity, bookToOrderPrice.get());

            this.store.put(bookType, new Book(
                    new BigDecimal(String.valueOf(orderedBookPrice)),
                    orderedBookNewQuantity
            ));

            this.report.updateTotalSales(bookType);
            this.report.calculateTotalProfit(bookType);

        } else {
            this.restock(bookType, bookToOrderQuantity.get());
            throw new OutOfStockException("Book " + bookType + " is out of stock.");
        }
    }

    @Override
    public void restock(String bookType, Integer quantity) {
        if (quantity < _MIN_STOCK_) {
            Supplier<Stream<ConcurrentMap<String, Book>>> streamSupplier = () -> Stream.of(this.store);

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

            this.cashOut(bookToRestockPrice);

            this.store.put(bookType, new Book(
                    new BigDecimal(String.valueOf(bookToRestockPrice)),
                    bookToRestockNewQuantity
            ));
        }
    }

    @Override
    public void cashIn(Integer quantity, BigDecimal price) {
        var currentBudget = this.getBudget();
        var profit = price.multiply(new BigDecimal(quantity));
        this.setBudget(currentBudget.add(profit));
    }

    @Override
    public void cashOut(BigDecimal restockPrice) {
        var currentBudget = this.getBudget();
        var pricePerBook = restockPrice.multiply(_SUPPLIER_RATE_).setScale(2, RoundingMode.CEILING);
        var totalOrder = pricePerBook.multiply(new BigDecimal("10.00").setScale(2, RoundingMode.CEILING));
        this.setBudget(currentBudget.subtract(totalOrder).setScale(2, RoundingMode.CEILING));
    }

    @Override
    public void setCurrentStore(ConcurrentMap<String, Book> store) {
        this.store = store;
    }

    @Override
    public ConcurrentMap<String, Book> getCurrentStore() {
        return this.store;
    }

    @Override
    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    @Override
    public BigDecimal getBudget() {
        return this.budget;
    }
}
