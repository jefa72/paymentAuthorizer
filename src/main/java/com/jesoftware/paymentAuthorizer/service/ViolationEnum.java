package com.jesoftware.paymentAuthorizer.service;

public enum ViolationEnum {

    INSUFICIENT_LIMIT("insuficient-limit"),
    PAYMENT_SESSION_ALREADY_INITIALIZED("payment-session-already-initialized"),
    PAYMENT_SESSION_NOT_INITIALIZED("paymentsession-not-initialized"),
    PAYMENT_RULES_NOT_INITIALIZED("paymentrules-not-initialized");

    private String name;

    ViolationEnum(String s) {
        this.name = s;
    }
}
