package org.shop.app;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TestVariables {

    BASIC_CLIENT_NAME("mock"),

    BASIC_CLIENT_PS("mock"),

    BASIC_ORDER("Iphone 11");

    private String testProperty;
}
