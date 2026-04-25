package com.project.ecommerce.domain.payment.controller;

import com.project.ecommerce.domain.payment.dto.PaymentResponseDTO;
import com.project.ecommerce.domain.payment.dto.StripePaymentResponseDTO;
import com.project.ecommerce.domain.payment.service.PaymentService;
import com.project.ecommerce.infra.stripe.StripeWebhookHandler;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final StripeWebhookHandler stripeWebhookHandler;

    @PostMapping("/{orderId}")
    public ResponseEntity<StripePaymentResponseDTO> createPayment(
            @PathVariable UUID orderId) throws StripeException {
        StripePaymentResponseDTO response = paymentService.createPayment(orderId);
        return ResponseEntity
                .created(URI.create("/payments/order/" + orderId))
                .body(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) throws StripeException {
        stripeWebhookHandler.handle(payload, sigHeader);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(
            @PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
}