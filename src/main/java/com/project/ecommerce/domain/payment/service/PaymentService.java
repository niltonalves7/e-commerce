package com.project.ecommerce.domain.payment.service;

import com.project.ecommerce.domain.order.entity.Order;
import com.project.ecommerce.domain.order.service.OrderService;
import com.project.ecommerce.domain.payment.dto.PaymentResponseDTO;
import com.project.ecommerce.domain.payment.dto.StripePaymentResponseDTO;
import com.project.ecommerce.domain.payment.entity.Payment;
import com.project.ecommerce.domain.payment.mapper.PaymentMapper;
import com.project.ecommerce.domain.payment.repository.PaymentRepository;
import com.project.ecommerce.infra.stripe.StripeService;
import com.project.ecommerce.infra.exception.BusinessException;
import com.project.ecommerce.infra.exception.ResourceNotFoundException;
import com.project.ecommerce.shared.enums.OrderStatus;
import com.project.ecommerce.shared.enums.PaymentStatus;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final PaymentMapper paymentMapper;
    private final StripeService stripeService;

    @Transactional
    public StripePaymentResponseDTO createPayment(UUID orderId) throws StripeException {
        Order order = orderService.getOrderEntity(orderId);
        validateOrder(order);

        Optional<Payment> existingPayment = paymentRepository.findByOrderId(orderId);
        if (existingPayment.isPresent()) {
            Payment payment = existingPayment.get();

            if (payment.getStatus() == PaymentStatus.PAID) {
                throw new BusinessException("Order already paid");
            }

            PaymentIntent intent = stripeService.retrievePaymentIntent(
                    payment.getStripePaymentIntentId()
            );

            return new StripePaymentResponseDTO(
                    payment.getId(),
                    intent.getClientSecret(),
                    payment.getStatus()
            );
        }

        PaymentIntent paymentIntent = stripeService.createPaymentIntent(
                order.getTotalAmount(), "brl"
        );

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .status(PaymentStatus.PENDING)
                .stripePaymentIntentId(paymentIntent.getId())
                .build();

        paymentRepository.save(payment);

        return new StripePaymentResponseDTO(
                payment.getId(),
                paymentIntent.getClientSecret(),
                payment.getStatus()
        );
    }

    @Transactional
    public void handleWebhook(String stripePaymentIntentId, PaymentStatus status) {
        Payment payment = paymentRepository.findByStripePaymentIntentId(stripePaymentIntentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        payment.setStatus(status);
        paymentRepository.save(payment);

        if (status == PaymentStatus.PAID) {
            orderService.updateOrderStatus(payment.getOrder().getId(), OrderStatus.PROCESSING);
        } else if (status == PaymentStatus.FAILED) {
            orderService.updateOrderStatus(payment.getOrder().getId(), OrderStatus.CANCELED);
        }
    }

    public PaymentResponseDTO getPaymentByOrderId(UUID orderId) {
        return paymentMapper.toResponse(
                paymentRepository.findByOrderId(orderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Payment not found"))
        );
    }

    private void validateOrder(Order order) {
        if (order.getItems().isEmpty()) {
            throw new BusinessException("Cannot create payment for an empty order");
        }
    }
}