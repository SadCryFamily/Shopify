package org.shop.app.exception;

public class ClientAlreadyDeletedException extends RuntimeException {

    public ClientAlreadyDeletedException(String message) {
        super(message);
    }
}
