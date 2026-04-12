package com.project.ecomerce.payment.mapper;

import com.project.ecomerce.payment.dto.PaymentResponseDTO;
import com.project.ecomerce.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "order.id", target = "orderId")
    PaymentResponseDTO toResponse(Payment payment);
}