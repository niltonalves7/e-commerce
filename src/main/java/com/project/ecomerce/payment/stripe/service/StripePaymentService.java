package com.project.ecomerce.payment.stripe.service;

import com.project.ecomerce.common.enums.OrderStatus;
import com.project.ecomerce.common.enums.PaymentStatus;
import com.project.ecomerce.order.entity.Order;
import com.project.ecomerce.order.service.OrderService;
import com.project.ecomerce.payment.entity.Payment;
import com.project.ecomerce.payment.repository.PaymentRepository;
import com.project.ecomerce.payment.stripe.dto.StripePaymentResponseDTO;
import com.project.ecomerce.exception.BusinessException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class StripePaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final StripeService stripeService;

    public StripePaymentService(PaymentRepository paymentRepository,
                                OrderService orderService,
                                StripeService stripeService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
        this.stripeService = stripeService;
    }

    @Transactional
    public StripePaymentResponseDTO createStripePayment(UUID orderId) throws StripeException {

        Order order = orderService.getOrderEntityById(orderId);
        if (order.getItems().isEmpty()) {
            throw new BusinessException("It's not possible to create a payment for an order without items.");
        }

        BigDecimal amount = order.getTotalAmount();
        PaymentIntent paymentIntent = stripeService.createPaymentIntent(amount, "brl");

        Payment payment = Payment.builder()
                .order(order)
                .amount(amount)
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
    public void updateStripePaymentStatus(String stripePaymentId, PaymentStatus status) {

        Payment payment = paymentRepository.findByStripePaymentIntentId(stripePaymentId)
                .orElseThrow(() -> new BusinessException("Payment not found"));

        payment.setStatus(status);
        paymentRepository.save(payment);

        if (status == PaymentStatus.PAID) {
            orderService.updateOrderStatus(
                    payment.getOrder().getId(),
                    OrderStatus.PROCESSING);
        }
    }
}

