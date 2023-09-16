package org.shop.app.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessage {

    CLIENT_ALREADY_CREATED("Client already created by ID/Username provided"),

    CLIENT_NOT_FOUND("Unable to retrieve client by ID provided"),

    ACCOUNT_NOT_AUTHORIZED("Unfortunately, you cannot reach the resources while unauthorized."),

    ACCOUNT_ACCESS_DENIED("Opps.. You don't have a authority to enter the resource"),

    ORDER_NOT_ENOUGH_STOCK("Cannot buy more units than provided in order"),

    ORDER_NULL("Cannot find order by given data"),

    ORDER_ALREADY_CREATED("Order already created by order name provided"),

    ORDER_NOT_MY("You cannot pay for foreign order"),

    ORDER_CLIENT_ALREADY_PRESENT("Order already placed for you"),

    CLIENT_ALREADY_DELETED("Client already deleted by ID provided");

    private String exceptionMessage;

    ExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
