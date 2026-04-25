package com.project.ecomerce.domain.user.mapper;

import com.project.ecomerce.domain.user.dto.request.RegisterUserRequestDTO;
import com.project.ecomerce.domain.user.dto.response.RegisterUserResponseDTO;
import com.project.ecomerce.domain.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegisterUserRequestDTO request);

    RegisterUserResponseDTO toRegisterResponse(User user);
}