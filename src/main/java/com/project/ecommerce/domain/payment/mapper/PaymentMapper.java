package com.project.ecommerce.domain.payment.mapper;

import com.project.ecommerce.domain.payment.dto.PaymentResponseDTO;
import com.project.ecommerce.domain.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "order.id", target = "orderId")
    PaymentResponseDTO toResponse(Payment payment);
}