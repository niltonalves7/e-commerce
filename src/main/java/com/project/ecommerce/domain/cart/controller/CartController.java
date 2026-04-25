package com.project.ecommerce.domain.cart.controller;

import com.project.ecommerce.domain.cart.dto.request.AddCartItemRequestDTO;
import com.project.ecommerce.domain.cart.dto.request.UpdateCartItemRequestDTO;
import com.project.ecommerce.domain.cart.dto.response.CartResponseDTO;
import com.project.ecommerce.domain.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItem(
            @RequestBody @Valid AddCartItemRequestDTO request) {
        return ResponseEntity.ok(cartService.addItem(request));
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> updateItem(
            @PathVariable UUID itemId,
            @RequestBody @Valid UpdateCartItemRequestDTO request) {
        return ResponseEntity.ok(cartService.updateItem(itemId, request));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> removeItem(@PathVariable UUID itemId) {
        return ResponseEntity.ok(cartService.removeItem(itemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}