package com.mytutor.bookshop;

import com.mytutor.bookshop.infrastructure.InMemoryStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class BookshopApplication {

    public static void main(String[] args) {
        InMemoryStorage storage = new InMemoryStorage();
        storage.createNewStore(new BigDecimal("500.00"));
        SpringApplication.run(BookshopApplication.class, args);
    }

}
