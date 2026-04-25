package com.project.ecommerce.domain.cart.service;

import com.project.ecommerce.domain.cart.dto.request.AddCartItemRequestDTO;
import com.project.ecommerce.domain.cart.dto.request.UpdateCartItemRequestDTO;
import com.project.ecommerce.domain.cart.dto.response.CartResponseDTO;
import com.project.ecommerce.domain.cart.entity.Cart;
import com.project.ecommerce.domain.cart.entity.CartItem;
import com.project.ecommerce.domain.cart.mapper.CartMapper;
import com.project.ecommerce.domain.cart.repository.CartItemRepository;
import com.project.ecommerce.domain.cart.repository.CartRepository;
import com.project.ecommerce.domain.product.entity.Product;
import com.project.ecommerce.domain.product.repository.ProductRepository;
import com.project.ecommerce.domain.user.entity.User;
import com.project.ecommerce.domain.user.repository.UserRepository;
import com.project.ecommerce.infra.exception.BusinessException;
import com.project.ecommerce.infra.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper mapper;

    public CartResponseDTO getCart() {
        User user = getAuthenticatedUser();
        Cart cart = getOrCreateCart(user);
        return mapper.toResponse(cart);
    }

    @Transactional
    public CartResponseDTO addItem(AddCartItemRequestDTO request) {
        User user = getAuthenticatedUser();
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStockQuantity() < request.quantity()) {
            throw new BusinessException("Insufficient stock");
        }

        // se produto já está no carrinho, incrementa a quantidade
        cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .ifPresentOrElse(
                        existingItem -> existingItem.setQuantity(existingItem.getQuantity() + request.quantity()),
                        () -> {
                            CartItem newItem = CartItem.builder()
                                    .cart(cart)
                                    .product(product)
                                    .quantity(request.quantity())
                                    .unitPrice(product.getPrice())
                                    .build();
                            cart.getItems().add(newItem);
                        }
                );

        return mapper.toResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponseDTO updateItem(UUID itemId, UpdateCartItemRequestDTO request) {
        User user = getAuthenticatedUser();
        Cart cart = getOrCreateCart(user);

        CartItem item = findCartItem(itemId);
        validateCartOwnership(cart, item);

        if (item.getProduct().getStockQuantity() < request.quantity()) {
            throw new BusinessException("Insufficient stock");
        }

        item.setQuantity(request.quantity());
        return mapper.toResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponseDTO removeItem(UUID itemId) {
        User user = getAuthenticatedUser();
        Cart cart = getOrCreateCart(user);

        CartItem item = findCartItem(itemId);
        validateCartOwnership(cart, item);

        cart.getItems().remove(item);
        return mapper.toResponse(cartRepository.save(cart));
    }

    @Transactional
    public void clearCart() {
        User user = getAuthenticatedUser();
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // método usado pelo OrderService no checkout
    public Cart getCartEntity() {
        return getOrCreateCart(getAuthenticatedUser());
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .build()
                ));
    }

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private CartItem findCartItem(UUID itemId) {
        return cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
    }

    private void validateCartOwnership(Cart cart, CartItem item) {
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new BusinessException("Item does not belong to user's cart");
        }
    }
}