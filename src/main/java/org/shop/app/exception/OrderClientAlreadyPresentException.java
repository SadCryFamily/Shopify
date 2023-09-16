package org.shop.app.exception;

public class OrderClientAlreadyPresentException extends RuntimeException {

    public OrderClientAlreadyPresentException(String message) {
        super(message);
    }
}
