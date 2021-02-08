package com.mytutor.bookshop.infrastructure.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String message) {
        super(message);
    }
}
