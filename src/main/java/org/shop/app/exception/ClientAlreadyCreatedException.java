package org.shop.app.exception;

public class ClientAlreadyCreatedException extends RuntimeException {

    public ClientAlreadyCreatedException(String message) {
        super(message);
    }
}
