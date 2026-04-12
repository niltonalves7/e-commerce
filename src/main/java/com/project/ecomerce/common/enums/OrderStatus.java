package com.project.ecomerce.common.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING("pending"),         // pedido criado, aguardando pagamento
    PROCESSING("processing"),   // sendo preparado
    SHIPPED("shipped"),         // enviado
    DELIVERED("delivered"),     // entregue
    CANCELED("canceled"),       // cancelado
    REFUNDED("refunded");       // reembolsado

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public static OrderStatus fromValue(String value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid order status: " + value);
    }
}