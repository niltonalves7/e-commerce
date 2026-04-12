package com.project.ecomerce.order.mapper;

import com.project.ecomerce.order.dto.response.OrderItemResponseDTO;
import com.project.ecomerce.order.dto.response.OrderResponseDTO;
import com.project.ecomerce.order.entity.Order;
import com.project.ecomerce.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "items", target = "items")
    OrderResponseDTO toResponse(Order order);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemResponseDTO toItemResponse(OrderItem item);
}
