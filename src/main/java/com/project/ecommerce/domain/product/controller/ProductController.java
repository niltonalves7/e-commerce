package com.project.ecommerce.domain.product.controller;

import com.project.ecommerce.domain.product.dto.request.CreateProductRequestDTO;
import com.project.ecommerce.domain.product.dto.request.UpdateProductRequestDTO;
import com.project.ecommerce.domain.product.dto.response.ProductResponseDTO;
import com.project.ecommerce.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestBody @Valid CreateProductRequestDTO request) {
        ProductResponseDTO response = productService.createProduct(request);
        return ResponseEntity
                .created(URI.create("/products/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String name,
            Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(categoryId, name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateProductRequestDTO request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}