package com.mytutor.bookshop.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;


class ReportImplTest {

    @Test
    @DisplayName("Verify increment new sale")
    public void itShouldUpdateTotalSaleForSpecificBookType() {
        ReportImpl report = new ReportImpl();

        report.updateTotalSales("A");
        report.updateTotalSales("A");
        report.updateTotalSales("D");

        assertEquals(2, report.getTotalSales("A"));
        assertEquals(1, report.getTotalSales("D"));
    }

    @Test
    @DisplayName("Verify profit calculation")
    public void itShouldCalculateProfitPerBookPriceCorrectly() {
        ReportImpl report = new ReportImpl();

        report.updateTotalSales("A");
        report.updateTotalSales("A");
        report.updateTotalSales("A");
        report.updateTotalSales("B");
        report.updateTotalSales("B");
        report.updateTotalSales("B");
        report.updateTotalSales("B");

        var profitA = report.calculateTotalProfit("A");
        var profitB = report.calculateTotalProfit("B");

        assertEquals(new BigDecimal("75.00"), profitA);
        assertEquals(new BigDecimal("80.00"), profitB);

    }

    @Test
    @DisplayName("Verify concurrent sales")
    void itShouldUpdateDataWithConcurrentPurchasesAndAvoidRaceCondition() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(2);
        ReportImpl r = new ReportImpl();

        Callable<Void> purchase = () -> {
            latch.countDown();
            latch.await();
            r.updateTotalSales("E");
            return null;
        };

        Future<Void> t1 = executor.submit(purchase);
        Future<Void> t2 = executor.submit(purchase);

        t1.get();
        t2.get();
        executor.shutdown();

        assertEquals(2, r.getTotalSales("E"));
        assertEquals(new BigDecimal("54.00"), r.calculateTotalProfit("E"));
        assertEquals(new BigDecimal("554.00"), new BigDecimal("500.00").add(r.calculateTotalProfit("E")));
    }

    @Test
    @DisplayName("Verify report generator")
    void itShouldGenerateValidReportBasedOnSales() {
        ReportImpl report = new ReportImpl();

        report.updateTotalSales("A");
        report.updateTotalSales("A"); // 2 sales
        report.updateTotalSales("B"); // 1 sale
        report.updateTotalSales("C"); // 5 sales
        report.updateTotalSales("C");
        report.updateTotalSales("C");
        report.updateTotalSales("C");
        report.updateTotalSales("C");

        var r = report.getReport();
        assertEquals(generateReport(), r);
    }

    private ConcurrentMap<String, Map<Integer, BigDecimal>> generateReport() {
        ConcurrentMap<String, Map<Integer, BigDecimal>> report = new ConcurrentHashMap<>();
        Map<Integer, BigDecimal> s1 = new HashMap<>();
        s1.put(2, new BigDecimal("50.00"));
        Map<Integer, BigDecimal> s2 = new HashMap<>();
        s2.put(1, new BigDecimal("20.00"));
        Map<Integer, BigDecimal> s3 = new HashMap<>();
        s3.put(5, new BigDecimal("115.00"));
        Map<Integer, BigDecimal> s4 = new HashMap<>();
        s4.put(0, new BigDecimal("00.00"));
        Map<Integer, BigDecimal> s5 = new HashMap<>();
        s5.put(0, new BigDecimal("00.00"));

        report.put("A", s1);
        report.put("B", s2);
        report.put("C", s3);
        report.put("D", s4);
        report.put("E", s5);

        return report;
    }
}