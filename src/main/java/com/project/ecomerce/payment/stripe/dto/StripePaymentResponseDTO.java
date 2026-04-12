package com.project.ecomerce.payment.stripe.dto;

import java.util.UUID;
import com.project.ecomerce.common.enums.PaymentStatus;

public record StripePaymentResponseDTO(
        UUID paymentId,
        String clientSecret,
        PaymentStatus status
) {}
