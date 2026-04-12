package com.project.ecomerce.product.mapper;

import com.project.ecomerce.product.dto.request.CreateProductRequestDTO;
import com.project.ecomerce.product.dto.response.ProductResponseDTO;
import com.project.ecomerce.product.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(CreateProductRequestDTO request);

    ProductResponseDTO toResponse(Product product);
}