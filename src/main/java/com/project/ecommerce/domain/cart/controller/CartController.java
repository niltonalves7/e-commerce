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
    public ResponseEntity<CartResponseDTO> addItemToCart(
            @RequestBody @Valid AddCartItemRequestDTO request) {
        return ResponseEntity.ok(cartService.addItemToCart(request));
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> updateItemToCart(
            @PathVariable UUID itemId,
            @RequestBody @Valid UpdateCartItemRequestDTO request) {
        return ResponseEntity.ok(cartService.updateCartItem(itemId, request));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(@PathVariable UUID itemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(itemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}