package org.shop.app.exception;

public class AuthorityAlreadyExistsException extends RuntimeException {

    public AuthorityAlreadyExistsException(String message) {
        super(message);
    }
}
