package com.project.ecommerce.domain.cart.mapper;

import com.project.ecommerce.domain.cart.dto.response.CartItemResponseDTO;
import com.project.ecommerce.domain.cart.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productImage", source = "product.imageUrl")
    CartItemResponseDTO toItemResponse(CartItem item);
}