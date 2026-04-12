package com.project.ecomerce.product.service;

import com.project.ecomerce.product.dto.request.CreateProductRequestDTO;
import com.project.ecomerce.product.dto.response.ProductResponseDTO;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponseDTO createProduct(CreateProductRequestDTO productDto);
    ProductResponseDTO getProductById(UUID id);
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO updateProduct(UUID id, CreateProductRequestDTO productDto);
    void deleteProduct(UUID id);
}
