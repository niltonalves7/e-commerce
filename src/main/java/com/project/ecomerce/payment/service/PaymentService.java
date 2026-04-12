package com.project.ecomerce.payment.service;

import com.project.ecomerce.payment.entity.Payment;

import java.util.UUID;

public interface PaymentService {

    Payment createPayment(UUID orderId);
    Payment getPaymentByOrderId(UUID orderId);
    void deletePaymentByOrderId(UUID orderId);
}