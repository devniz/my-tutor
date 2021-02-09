package com.mytutor.bookshop.infrastructure;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public interface Report {
    void updateTotalSales(String bookType);

    void setTotalSales(String bookType, Integer totalSales, BigDecimal price);

    ConcurrentMap<String, Map<Integer, BigDecimal>> getReport();

    Integer getTotalSales(String bookType);

    BigDecimal getBookPrice(String bookType);

    BigDecimal calculateTotalProfit(String bookType);

}
