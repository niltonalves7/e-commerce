package com.project.ecommerce.domain.order.mapper;

import com.project.ecommerce.domain.order.dto.response.OrderItemResponseDTO;
import com.project.ecommerce.domain.order.dto.response.OrderResponseDTO;
import com.project.ecommerce.domain.order.entity.Order;
import com.project.ecommerce.domain.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "items", target = "items")
    OrderResponseDTO toResponse(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemResponseDTO toItemResponse(OrderItem item);
}