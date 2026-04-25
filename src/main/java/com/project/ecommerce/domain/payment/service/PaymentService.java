package com.project.ecommerce.domain.payment.service;

import com.project.ecommerce.infra.exception.BusinessException;
import com.project.ecommerce.infra.exception.ResourceNotFoundException;
import com.project.ecommerce.domain.order.entity.Order;
import com.project.ecommerce.domain.order.repository.OrderRepository;
import com.project.ecommerce.domain.payment.entity.Payment;
import com.project.ecommerce.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Payment createPayment(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new BusinessException("Cannot create payment for an empty order");
        }

        paymentRepository.findByOrderId(orderId)
                .ifPresent(p -> {
                    throw new BusinessException("Payment already exists for this order");
                });

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());

        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentByOrderId(UUID orderId) {

        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException("Order not found for this payment"));
    }

    @Override
    public void deletePaymentByOrderId(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException("Payment not found for this order"));

        paymentRepository.delete(payment);
    }
}