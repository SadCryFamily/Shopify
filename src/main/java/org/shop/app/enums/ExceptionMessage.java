package org.shop.app.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessage {

    CLIENT_ALREADY_CREATED("Client already created by ID provided"),

    CLIENT_NOT_FOUND("Unable to retrieve client by ID provided"),

    ACCOUNT_NOT_AUTHORIZED("Unfortunately, you cannot reach the resources while unauthorized."),

    CLIENT_ALREADY_DELETED("Client already deleted by ID provided");

    private String exceptionMessage;

    ExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
