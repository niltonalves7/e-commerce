package com.project.ecomerce.order.service;

import com.project.ecomerce.common.enums.OrderStatus;
import com.project.ecomerce.order.dto.request.AddItemRequestDTO;
import com.project.ecomerce.order.dto.request.CreateOrderRequestDTO;
import com.project.ecomerce.order.dto.response.OrderResponseDTO;
import com.project.ecomerce.order.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponseDTO createOrder(CreateOrderRequestDTO request);
    OrderResponseDTO addItem(UUID orderId, AddItemRequestDTO request);
    OrderResponseDTO getOrderById(UUID orderId);
    List<OrderResponseDTO> getAllOrders();
    Order getOrderEntityById(UUID orderId);
    void updateOrderStatus(UUID orderId, OrderStatus status);
    void removeItem(UUID orderId, UUID itemId);
}
