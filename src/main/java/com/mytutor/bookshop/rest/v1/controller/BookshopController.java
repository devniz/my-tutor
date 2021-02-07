package com.mytutor.bookshop.rest.v1.controller;

import com.mytutor.bookshop.domain.OrderBook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/shop")
public class BookshopController {

    @PostMapping(path = "/order")
    public void orderBook(@RequestBody OrderBook order) {
    }

    @GetMapping(path = "/report")
    public void getReport() {
    }
}
