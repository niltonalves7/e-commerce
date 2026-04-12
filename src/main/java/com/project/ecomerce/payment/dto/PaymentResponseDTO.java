package com.project.ecomerce.payment.dto;

import com.project.ecomerce.common.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponseDTO(

        UUID id,
        UUID orderId,
        BigDecimal amount,
        PaymentStatus status,
        String stripePaymentIntentId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}