package com.project.ecommerce.domain.order.service;

import com.project.ecommerce.domain.cart.entity.Cart;
import com.project.ecommerce.domain.cart.entity.CartItem;
import com.project.ecommerce.domain.cart.service.CartService;
import com.project.ecommerce.domain.order.dto.response.OrderResponseDTO;
import com.project.ecommerce.domain.order.entity.Order;
import com.project.ecommerce.domain.order.entity.OrderItem;
import com.project.ecommerce.domain.order.mapper.OrderMapper;
import com.project.ecommerce.domain.order.repository.OrderRepository;
import com.project.ecommerce.domain.user.entity.User;
import com.project.ecommerce.domain.user.repository.UserRepository;
import com.project.ecommerce.infra.exception.BusinessException;
import com.project.ecommerce.infra.exception.ResourceNotFoundException;
import com.project.ecommerce.shared.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDTO checkout() {
        User user = getAuthenticatedUser();
        Cart cart = cartService.getCartEntity();

        if (cart.getItems().isEmpty()) {
            throw new BusinessException("Cart is empty");
        }

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(this::toOrderItem)
                .toList();

        orderItems.forEach(item -> {
            if (item.getProduct().getStockQuantity() < item.getQuantity()) {
                throw new BusinessException(
                        "Insufficient stock for product: " + item.getProduct().getName()
                );
            }
        });

        Order order = Order.builder()
                .user(user)
                .build();

        orderItems.forEach(item -> {
            item.setOrder(order);
            order.getItems().add(item);
        });

        order.setTotalAmount(calculateTotal(order));

        Order saved = orderRepository.save(order);
        cartService.clearCart();

        return orderMapper.toResponse(saved);
    }

    public OrderResponseDTO getOrderById(UUID orderId) {
        Order order = findOrder(orderId);
        validateOrderOwnership(order);
        return orderMapper.toResponse(order);
    }

    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        User user = getAuthenticatedUser();
        return orderRepository.findByUserId(user.getId(), pageable)
                .map(orderMapper::toResponse);
    }

    @Transactional
    public void updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = findOrder(orderId);

        if (!order.getStatus().canTransitionTo(newStatus)) {
            throw new BusinessException("Invalid status transition: "
                    + order.getStatus() + " → " + newStatus);
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    public Order getOrderEntity(UUID orderId) {
        return findOrder(orderId);
    }

    private OrderItem toOrderItem(CartItem cartItem) {
        return OrderItem.builder()
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .build();
    }

    private BigDecimal calculateTotal(Order order) {
        return order.getItems().stream()
                .map(item -> item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order findOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    private void validateOrderOwnership(Order order) {
        User user = getAuthenticatedUser();
        if (!order.getUser().getId().equals(user.getId())) {
            throw new BusinessException("Order does not belong to the authenticated user");
        }
    }

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}