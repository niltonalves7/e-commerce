package com.project.ecommerce.domain.category.mapper;

import com.project.ecommerce.domain.category.dto.request.CreateCategoryRequestDTO;
import com.project.ecommerce.domain.category.dto.request.UpdateCategoryRequestDTO;
import com.project.ecommerce.domain.category.dto.response.CategoryResponseDTO;
import com.project.ecommerce.domain.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CreateCategoryRequestDTO request);

    CategoryResponseDTO toResponse(Category category);

    void updateEntityFromDTO(UpdateCategoryRequestDTO request, @MappingTarget Category category);
}