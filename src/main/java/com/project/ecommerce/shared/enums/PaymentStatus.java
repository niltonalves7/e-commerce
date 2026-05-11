package com.project.ecommerce.shared.enums;

public enum PaymentStatus {

    PENDING,
    PROCESSING,
    PAID,
    FAILED,
    CANCELED,
    REFUNDED;

    public static PaymentStatus fromValue(String value) {
        try {
            return PaymentStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid payment status: " + value);
        }
    }
}