package com.project.ecommerce.domain.category.service;

import com.project.ecommerce.domain.category.dto.request.CreateCategoryRequestDTO;
import com.project.ecommerce.domain.category.dto.request.UpdateCategoryRequestDTO;
import com.project.ecommerce.domain.category.dto.response.CategoryResponseDTO;
import com.project.ecommerce.domain.category.entity.Category;
import com.project.ecommerce.domain.category.mapper.CategoryMapper;
import com.project.ecommerce.domain.category.repository.CategoryRepository;
import com.project.ecommerce.infra.exception.AlreadyExistsException;
import com.project.ecommerce.infra.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    public CategoryResponseDTO createCategory(CreateCategoryRequestDTO request) {
        if (categoryRepository.findByName(request.name()).isPresent()) {
            throw new AlreadyExistsException("Category already exists");
        }

        return mapper.toResponse(categoryRepository.save(mapper.toEntity(request)));
    }

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public CategoryResponseDTO updateCategory(UUID id, UpdateCategoryRequestDTO request) {
        Category category = findCategoryById(id);

        categoryRepository.findByName(request.name())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new AlreadyExistsException("Category name already in use");
                });

        mapper.updateEntityFromDTO(request, category);
        return mapper.toResponse(categoryRepository.save(category));
    }

    public void deleteCategory(UUID id) {
        categoryRepository.delete(findCategoryById(id));
    }

    private Category findCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
}