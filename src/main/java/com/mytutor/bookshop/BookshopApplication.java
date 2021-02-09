package com.mytutor.bookshop;

import com.mytutor.bookshop.infrastructure.InMemoryStorageImpl;
import com.mytutor.bookshop.infrastructure.ReportImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class BookshopApplication {

    public static void main(String[] args) {
        ReportImpl report = new ReportImpl();
        InMemoryStorageImpl storage = new InMemoryStorageImpl(report);
        storage.createNewStore(new BigDecimal("500.00"));
        SpringApplication.run(BookshopApplication.class, args);
    }

}
