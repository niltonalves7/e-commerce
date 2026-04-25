package com.project.ecomerce.domain.product.controller;

import com.project.ecomerce.domain.product.dto.request.CreateProductRequestDTO;
import com.project.ecomerce.domain.product.dto.response.ProductResponseDTO;
import com.project.ecomerce.domain.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) { this.productService = productService; }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct( @Valid @RequestBody CreateProductRequestDTO productDto ) {

        ProductResponseDTO response = productService.createProduct(productDto);
        return ResponseEntity
                .created(URI.create("/products/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct( @PathVariable UUID id, @Valid @RequestBody CreateProductRequestDTO productDto ) {
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}