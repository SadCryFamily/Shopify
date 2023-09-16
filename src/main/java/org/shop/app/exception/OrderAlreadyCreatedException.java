package org.shop.app.exception;

public class OrderAlreadyCreatedException extends RuntimeException {

    public OrderAlreadyCreatedException(String message) {
        super(message);
    }
}
