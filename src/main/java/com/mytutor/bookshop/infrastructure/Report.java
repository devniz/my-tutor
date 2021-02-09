package com.mytutor.bookshop.infrastructure;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public interface Report {
    void updateTotalSales(String bookType, Integer quantity);

    void setSales(String bookType, Integer totalSales, BigDecimal price);

    ConcurrentMap<String, Map<Integer, BigDecimal>> getReport();

    Integer getSales(String bookType);

    BigDecimal getBookPrice(String bookType);

    BigDecimal calculateTotalProfit(String bookType);

}
