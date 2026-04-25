package com.project.ecomerce.domain.payment.controller;

import com.project.ecomerce.domain.payment.dto.PaymentResponseDTO;
import com.project.ecomerce.domain.payment.entity.Payment;
import com.project.ecomerce.domain.payment.mapper.PaymentMapper;
import com.project.ecomerce.domain.payment.service.PaymentService;
import com.project.ecomerce.domain.payment.stripe.dto.StripePaymentResponseDTO;
import com.project.ecomerce.domain.payment.stripe.service.StripePaymentService;
import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final StripePaymentService stripepaymentService;
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    public PaymentController(StripePaymentService stripepaymentService, PaymentService paymentService,
                             PaymentMapper paymentMapper) {
        this.stripepaymentService = stripepaymentService;
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<StripePaymentResponseDTO> createStripePayment(@PathVariable UUID orderId) throws StripeException {
        return ResponseEntity.ok(stripepaymentService.createStripePayment(orderId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(@PathVariable UUID orderId ) {
        Payment payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(paymentMapper.toResponse(payment));
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<?> deletePaymentById(@PathVariable UUID orderId) {
        paymentService.deletePaymentByOrderId(orderId);
        return ResponseEntity.noContent().build();
    }
}