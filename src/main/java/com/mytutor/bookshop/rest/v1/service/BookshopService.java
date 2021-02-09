package com.mytutor.bookshop.rest.v1.service;

import com.mytutor.bookshop.BookshopApplication;
import com.mytutor.bookshop.infrastructure.InMemoryStorageImpl;
import com.mytutor.bookshop.infrastructure.exception.QuantityExceedsStockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
public class BookshopService {

    private final InMemoryStorageImpl storage;

    @Autowired
    public BookshopService() {
        this.storage = BookshopApplication.getStorage();
    }

    public String orderBook(String bookType, Integer quantity) {
        if (quantity > 10) {
            throw new QuantityExceedsStockException("the maximum quantity you can order is 10.");
        }

        if (validateBookType(bookType)) {
            try {
                this.storage.updateBookStock(bookType.toUpperCase(), quantity);
            } catch (Exception ex) {
                log.error(ex.getMessage());
                return "Sorry, we are out of stock.";
            }
        }
        return "Thank you for your purchase!";
    }

    public String getReport() {
        var report = this.storage.generateReport();
        var currentBudget = this.storage.getBudget();

        StringBuilder strBuilder = new StringBuilder("MyTutor Bookshop Balance: ");
        strBuilder.append(currentBudget).append("\n");
        strBuilder.append("--------------------------------\n");

        for (Map.Entry<String, Map<Integer, BigDecimal>> entry : report.entrySet()) {
            String key = entry.getKey();
            Map<Integer, BigDecimal> values = entry.getValue();

            var soldCopies = values.keySet().stream().findFirst().get();
            var profitPerBook = values.values().stream().findFirst().get();

            strBuilder.append("Book ")
                    .append(key).append(" | ")
                    .append(soldCopies)
                    .append(" copies sold")
                    .append(" | £")
                    .append(profitPerBook)
                    .append(" Total Profit")
                    .append("\n");
        }

        return strBuilder.toString();
    }

    private boolean validateBookType(String bookType) {
        var allBookType = new String[]{"A", "B", "C", "D", "E"};
        return ArrayUtils.contains(allBookType, bookType.toUpperCase());
    }
}
