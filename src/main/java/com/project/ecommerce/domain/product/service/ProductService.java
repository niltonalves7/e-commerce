package com.project.ecommerce.domain.product.service;

import com.project.ecommerce.domain.category.entity.Category;
import com.project.ecommerce.domain.category.repository.CategoryRepository;
import com.project.ecommerce.domain.product.dto.request.CreateProductRequestDTO;
import com.project.ecommerce.domain.product.dto.request.UpdateProductRequestDTO;
import com.project.ecommerce.domain.product.dto.response.ProductResponseDTO;
import com.project.ecommerce.domain.product.entity.Product;
import com.project.ecommerce.domain.product.mapper.ProductMapper;
import com.project.ecommerce.domain.product.repository.ProductRepository;
import com.project.ecommerce.infra.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    public ProductResponseDTO createProduct(CreateProductRequestDTO request) {
        Category category = findCategoryById(request.categoryId());

        Product product = mapper.toEntity(request);
        product.setCategory(category);

        return mapper.toResponse(productRepository.save(product));
    }

    public Page<ProductResponseDTO> getAllProducts(UUID categoryId, String name, Pageable pageable) {
        if (categoryId != null && name != null) {
            return productRepository.findByCategoryIdAndNameContainingIgnoreCase(categoryId, name, pageable)
                    .map(mapper::toResponse);
        } else if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId, pageable)
                    .map(mapper::toResponse);
        } else if (name != null) {
            return productRepository.findByNameContainingIgnoreCase(name, pageable)
                    .map(mapper::toResponse);
        }
        return productRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    public ProductResponseDTO getProductById(UUID id) {
        return mapper.toResponse(findProductById(id));
    }

    public ProductResponseDTO updateProduct(UUID id, UpdateProductRequestDTO request) {
        Product product = findProductById(id);

        if (request.categoryId() != null) {
            product.setCategory(findCategoryById(request.categoryId()));
        }

        mapper.updateEntityFromDTO(request, product);
        return mapper.toResponse(productRepository.save(product));
    }

    public void deleteProduct(UUID id) {
        productRepository.delete(findProductById(id));
    }

    private Product findProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private Category findCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
}