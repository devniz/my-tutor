package com.mytutor.bookshop.rest.v1.controller;

import com.mytutor.bookshop.rest.v1.service.BookshopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping(path = "/api/v1/shop")
public class BookshopController {

    private final BookshopService service;

    @Autowired
    public BookshopController(BookshopService service) {
        this.service = service;
    }

    @PostMapping(path = "/order")
    public ResponseEntity<Boolean> orderBook(@RequestParam String bookType, @RequestParam Integer quantity) {
        if (!bookType.isEmpty() && (quantity > 0 && quantity <= 10)) {
            return new ResponseEntity<>(this.service.orderBook(bookType, quantity), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(path = "/report")
    public ResponseEntity<String> getReport() {
        return new ResponseEntity<>(this.service.getReport(), HttpStatus.OK);
    }
}
