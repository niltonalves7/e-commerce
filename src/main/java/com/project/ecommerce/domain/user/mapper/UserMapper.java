package com.project.ecommerce.domain.user.mapper;

import com.project.ecommerce.domain.user.dto.request.CreateUserRequestDTO;
import com.project.ecommerce.domain.user.dto.request.UpdateUserRequestDTO;
import com.project.ecommerce.domain.user.dto.response.UserResponseDTO;
import com.project.ecommerce.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(CreateUserRequestDTO request);

    UserResponseDTO toResponse(User user);

    void updateEntityFromDTO(UpdateUserRequestDTO request, @MappingTarget User user);
}