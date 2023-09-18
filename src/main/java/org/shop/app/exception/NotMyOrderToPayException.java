package org.shop.app.exception;

public class NotMyOrderToPayException extends RuntimeException {

    public NotMyOrderToPayException(String message) {
        super(message);
    }
}
