package com.project.ecomerce.domain.product.mapper;

import com.project.ecomerce.domain.product.dto.request.CreateProductRequestDTO;
import com.project.ecomerce.domain.product.dto.response.ProductResponseDTO;
import com.project.ecomerce.domain.product.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(CreateProductRequestDTO request);

    ProductResponseDTO toResponse(Product product);
}