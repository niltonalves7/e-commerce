package com.project.ecommerce.domain.product.service;

import com.project.ecommerce.infra.exception.ResourceNotFoundException;
import com.project.ecommerce.domain.product.dto.request.CreateProductRequestDTO;
import com.project.ecommerce.domain.product.dto.response.ProductResponseDTO;
import com.project.ecommerce.domain.product.entity.Product;
import com.project.ecommerce.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import com.project.ecommerce.domain.product.mapper.ProductMapper;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponseDTO createProduct(CreateProductRequestDTO productDto) {
        Product product = productMapper.toEntity(productDto);
        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    @Override
    public ProductResponseDTO getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponseDTO updateProduct(UUID id, CreateProductRequestDTO productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        product.setName(productDto.name());
        product.setPrice(productDto.price());
        product.setDescription(productDto.description());
        product.setStock(productDto.stock());
        product.setImageUrl(productDto.imageUrl());

        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }

    @Override
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        productRepository.delete(product);
    }
}
