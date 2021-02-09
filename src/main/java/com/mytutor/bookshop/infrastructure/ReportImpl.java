package com.mytutor.bookshop.infrastructure;

import com.mytutor.bookshop.domain.ReportItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Component
public class ReportImpl implements Report {

    private ConcurrentMap<String, ReportItem> report;

    public ReportImpl() {
        this.report = new ConcurrentHashMap<>() {
            {
                put("A", new ReportItem(0, new BigDecimal("25.00")));
                put("B", new ReportItem(0, new BigDecimal("20.00")));
                put("C", new ReportItem(0, new BigDecimal("23.00")));
                put("D", new ReportItem(0, new BigDecimal("30.00")));
                put("E", new ReportItem(0, new BigDecimal("27.00")));
            }
        };
    }

    @Override
    synchronized public void updateTotalSales(String bookType) {
        var currentTotalSales = this.getTotalSales(bookType);
        var currentBookPrice = this.getBookPrice(bookType);
        var newTotalSales = currentTotalSales + 1;
        this.setTotalSales(bookType, newTotalSales, currentBookPrice);
    }

    @Override
    public BigDecimal calculateTotalProfit(String bookType) {
        Supplier<Stream<ConcurrentMap<String, ReportItem>>> streamSupplier = () -> Stream.of(this.report);

        Integer totalSaleByBook = streamSupplier
                .get()
                .map(s -> s.get(bookType).getTotalSales())
                .findFirst()
                .orElse(null);

        BigDecimal bookPrice = streamSupplier
                .get()
                .map(s -> s.get(bookType).getBookPrice())
                .findFirst()
                .orElse(null);

        return bookPrice.multiply(BigDecimal.valueOf(totalSaleByBook));
    }

    @Override
    public ConcurrentMap<String, Map<Integer, BigDecimal>> getReport() {
        ConcurrentMap<String, Map<Integer, BigDecimal>> report = new ConcurrentHashMap<>();

        for (ConcurrentMap.Entry<String, ReportItem> entry : this.report.entrySet()) {
            String key = entry.getKey();
            ReportItem item = entry.getValue();

            Integer totalSales = item.getTotalSales();

            ConcurrentMap<Integer, BigDecimal> salesInfo = new ConcurrentHashMap<>();
            salesInfo.put(totalSales, getBookPrice(key).multiply(BigDecimal.valueOf(totalSales)));
            report.put(key, salesInfo);
        }

        return report;
    }

    @Override
    public void setTotalSales(String bookType, Integer totalSales, BigDecimal price) {
        this.report.put(bookType, new ReportItem(totalSales, price));
    }

    @Override
    public Integer getTotalSales(String bookType) {
        Supplier<Stream<ConcurrentMap<String, ReportItem>>> streamSupplier = () -> Stream.of(this.report);

        return streamSupplier
                .get()
                .map(s -> s.get(bookType).getTotalSales())
                .findFirst()
                .orElse(null);
    }

    @Override
    public BigDecimal getBookPrice(String bookType) {
        Supplier<Stream<ConcurrentMap<String, ReportItem>>> streamSupplier = () -> Stream.of(this.report);

        return streamSupplier
                .get()
                .map(s -> s.get(bookType).getBookPrice())
                .findFirst()
                .orElse(null);
    }
}
