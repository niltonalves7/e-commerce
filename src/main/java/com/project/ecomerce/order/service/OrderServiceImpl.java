package com.project.ecomerce.order.service;

import com.project.ecomerce.client.entity.Client;
import com.project.ecomerce.client.repository.ClientRepository;
import com.project.ecomerce.common.enums.OrderStatus;
import com.project.ecomerce.exception.BusinessException;
import com.project.ecomerce.exception.ResourceNotFoundException;
import com.project.ecomerce.order.dto.request.AddItemRequestDTO;
import com.project.ecomerce.order.dto.request.CreateOrderRequestDTO;
import com.project.ecomerce.order.dto.response.OrderResponseDTO;
import com.project.ecomerce.order.entity.Order;
import com.project.ecomerce.order.entity.OrderItem;
import com.project.ecomerce.order.mapper.OrderMapper;
import com.project.ecomerce.order.repository.OrderRepository;
import com.project.ecomerce.product.entity.Product;
import com.project.ecomerce.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ClientRepository clientRepository,
                            ProductRepository productRepository,
                            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderResponseDTO createOrder(CreateOrderRequestDTO orderRequest) {

        Client client = clientRepository.findById(orderRequest.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Order order = new Order();
        order.setClient(client);
        order.setItems(new ArrayList<>());

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponseDTO addItem(UUID orderId, AddItemRequestDTO itemRequest) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Product product = productRepository.findById(itemRequest.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(itemRequest.quantity());

        item.setUnitPrice(product.getPrice());

        order.getItems().add(item);

        calculateTotal(order);

        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toResponse(updatedOrder);
    }

    @Override
    public OrderResponseDTO getOrderById(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    public Order getOrderEntityById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Order not found"));
    }

    private void calculateTotal(Order order) {

        BigDecimal total = order.getItems().stream()
                .map(item -> item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);
    }

    public void updateOrderStatus(UUID orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public void removeItem(UUID orderId, UUID orderItemId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderItem itemToRemove = order.getItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found"));

        order.getItems().remove(itemToRemove);

        if (order.getItems().isEmpty() || order.getTotalAmount().equals(BigDecimal.ZERO)) {
            orderRepository.delete(order);
            return;
        }

        calculateTotal(order);
        orderRepository.save(order);
    }
}