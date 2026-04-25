package com.project.ecommerce.domain.payment.dto.response;

import com.project.ecommerce.shared.enums.PaymentStatus;

import java.util.UUID;

public record StripePaymentResponseDTO(
        UUID paymentId,
        String clientSecret,
        PaymentStatus status
) {}