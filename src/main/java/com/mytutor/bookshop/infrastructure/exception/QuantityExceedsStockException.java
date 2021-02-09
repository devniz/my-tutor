package com.mytutor.bookshop.infrastructure.exception;

public class QuantityExceedsStockException extends RuntimeException {
    public QuantityExceedsStockException(String message) {
        super(message);
    }
}
