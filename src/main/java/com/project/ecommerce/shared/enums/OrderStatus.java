package com.project.ecommerce.shared.enums;

public enum OrderStatus {

    PENDING,        // pedido criado, aguardando pagamento
    PROCESSING,     // sendo preparado
    SHIPPED,        // enviado
    DELIVERED,      // entregue
    CANCELED,       // cancelado
    REFUNDED;       // reembolsado

    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case PENDING -> next == PROCESSING || next == CANCELED;
            case PROCESSING -> next == SHIPPED || next == CANCELED;
            case SHIPPED -> next == DELIVERED;
            case DELIVERED -> next == REFUNDED;
            default -> false;
        };
    }

    public static OrderStatus fromValue(String value) {
        try {
            return OrderStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + value);
        }
    }
}