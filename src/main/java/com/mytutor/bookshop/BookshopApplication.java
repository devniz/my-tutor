package com.mytutor.bookshop;

import com.mytutor.bookshop.infrastructure.InMemoryStorageImpl;
import com.mytutor.bookshop.infrastructure.ReportImpl;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class BookshopApplication {

    @Getter
    private static InMemoryStorageImpl storage;

    public static void main(String[] args) {
        ReportImpl report = new ReportImpl();
        storage = new InMemoryStorageImpl(report);
        storage.createNewStore(new BigDecimal("500.00"));
        SpringApplication.run(BookshopApplication.class, args);
    }

}
