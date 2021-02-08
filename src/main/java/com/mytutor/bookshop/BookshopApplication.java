package com.mytutor.bookshop;

import com.mytutor.bookshop.infrastructure.InMemoryStorageImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class BookshopApplication {

    public static void main(String[] args) {
        InMemoryStorageImpl storage = new InMemoryStorageImpl();
        storage.createNewStore(new BigDecimal("500.00"));
        SpringApplication.run(BookshopApplication.class, args);
    }

}
