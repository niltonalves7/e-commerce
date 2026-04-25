package com.project.ecommerce.domain.category.controller;

import com.project.ecommerce.domain.category.dto.request.CreateCategoryRequestDTO;
import com.project.ecommerce.domain.category.dto.request.UpdateCategoryRequestDTO;
import com.project.ecommerce.domain.category.dto.response.CategoryResponseDTO;
import com.project.ecommerce.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @RequestBody @Valid CreateCategoryRequestDTO request) {
        CategoryResponseDTO response = categoryService.createCategory(request);
        return ResponseEntity
                .created(URI.create("/categories/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateCategoryRequestDTO request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}