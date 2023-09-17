package org.shop.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TestVariables {

    BASIC_CLIENT_NAME("mock"),

    BASIC_CLIENT_PS("mock"),

    BASIC_ORDER_NAME("Iphone 11"),

    BASIC_ORDER_UNIT("20"),

    BASIC_ORDER_PRICE("500");

    private String testProperty;


}
