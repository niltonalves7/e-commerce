package com.project.ecommerce.domain.product.mapper;

import com.project.ecommerce.domain.category.mapper.CategoryMapper;
import com.project.ecommerce.domain.product.dto.request.CreateProductRequestDTO;
import com.project.ecommerce.domain.product.dto.request.UpdateProductRequestDTO;
import com.project.ecommerce.domain.product.dto.response.ProductResponseDTO;
import com.project.ecommerce.domain.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(CreateProductRequestDTO request);

    ProductResponseDTO toResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(UpdateProductRequestDTO request, @MappingTarget Product product);
}