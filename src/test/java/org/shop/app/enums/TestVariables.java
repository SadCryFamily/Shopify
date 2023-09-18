package org.shop.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TestVariables {

    BASIC_CLIENT_NAME("mock"),

    NORMAL_CLIENT_NAME("mockClient"),

    BASIC_CLIENT_PS("mock"),

    NORMAL_CLIENT_PS("mockPassword"),

    BASIC_ORDER("Iphone 11"),

    BASIC_ORDER_UNIT("20"),

    BASIC_ORDER_PRICE("560");

    private String testProperty;
}
