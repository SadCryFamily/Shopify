package org.shop.app.exception;

public class OrderNullException extends RuntimeException {

    public OrderNullException(String message) {
        super(message);
    }
}
