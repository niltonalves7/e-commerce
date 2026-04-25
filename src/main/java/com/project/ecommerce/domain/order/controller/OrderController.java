package com.project.ecomerce.domain.order.controller;

import com.project.ecomerce.domain.order.dto.request.AddItemRequestDTO;
import com.project.ecomerce.domain.order.dto.request.CreateOrderRequestDTO;
import com.project.ecomerce.domain.order.dto.response.OrderResponseDTO;
import com.project.ecomerce.domain.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder( @RequestBody @Valid CreateOrderRequestDTO orderRequest ) {
        OrderResponseDTO response = orderService.createOrder(orderRequest);

        return ResponseEntity
                .created(URI.create("/orders/" + response.id()))
                .body(response);
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponseDTO> addItem( @PathVariable UUID orderId, @RequestBody @Valid AddItemRequestDTO itemRequest ) {
        return ResponseEntity.ok(orderService.addItem(orderId, itemRequest));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById( @PathVariable UUID orderId ) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @DeleteMapping("/{orderId}/items/{orderItemId}")
    public ResponseEntity<?> removeItem( @PathVariable UUID orderId, @PathVariable UUID orderItemId ) {
        orderService.removeItem(orderId, orderItemId);
        return ResponseEntity.noContent().build();
    }
}
