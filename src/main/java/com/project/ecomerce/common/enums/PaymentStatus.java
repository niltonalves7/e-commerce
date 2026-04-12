package com.project.ecomerce.common.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    PENDING("pending"),             // aguardando pagamento
    PROCESSING("processing"),       // pagamento em processamento
    PAID("paid"),         // pagamento concluído
    FAILED("failed"),               // pagamento falhou
    CANCELED("canceled"),           // cancelado
    REFUNDED("refunded");           // reembolsado

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    // 🔹 Conversão segura de String → Enum
    public static PaymentStatus fromValue(String value) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid payment status: " + value);
    }
}