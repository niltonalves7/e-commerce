package com.project.ecommerce.shared.enums;

public enum PaymentStatus {

    PENDING,        // aguardando pagamento
    PROCESSING,     // pagamento em processamento
    PAID,           // pagamento concluído
    FAILED,         // pagamento falhou
    CANCELED,       // cancelado
    REFUNDED;       // reembolsado

    public static PaymentStatus fromValue(String value) {
        try {
            return PaymentStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid payment status: " + value);
        }
    }
}