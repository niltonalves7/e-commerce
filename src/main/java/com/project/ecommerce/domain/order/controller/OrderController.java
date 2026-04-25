package com.project.ecommerce.domain.order.controller;

import com.project.ecommerce.domain.order.dto.request.UpdateOrderStatusRequestDTO;
import com.project.ecommerce.domain.order.dto.response.OrderResponseDTO;
import com.project.ecommerce.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout() {
        OrderResponseDTO response = orderService.checkout();
        return ResponseEntity
                .created(URI.create("/orders/" + response.id()))
                .body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID orderId,
            @RequestBody @Valid UpdateOrderStatusRequestDTO request) {
        orderService.updateOrderStatus(orderId, request.status());
        return ResponseEntity.noContent().build();
    }
}