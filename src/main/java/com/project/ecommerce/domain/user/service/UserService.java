package com.project.ecommerce.domain.user.service;

import com.project.ecommerce.domain.user.dto.request.CreateUserRequestDTO;
import com.project.ecommerce.domain.user.dto.request.UpdateUserRequestDTO;
import com.project.ecommerce.domain.user.dto.response.UserResponseDTO;
import com.project.ecommerce.domain.user.entity.User;
import com.project.ecommerce.domain.user.mapper.UserMapper;
import com.project.ecommerce.domain.user.repository.UserRepository;
import com.project.ecommerce.shared.enums.Role;
import com.project.ecommerce.infra.exception.AlreadyExistsException;
import com.project.ecommerce.infra.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(CreateUserRequestDTO request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new AlreadyExistsException("Email already registered");
        }

        User user = mapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        if (user.getRole() == null || user.getRole() != Role.ADMIN) {
            user.setRole(Role.USER);
        }

        return mapper.toResponse(userRepository.save(user));
    }

    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    public UserResponseDTO getUserById(UUID id) {
        return mapper.toResponse(findUserById(id));
    }

    public UserResponseDTO updateUser(UUID id, UpdateUserRequestDTO request) {
        User user = findUserById(id);

        userRepository.findByEmail(request.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new AlreadyExistsException("Email already in use");
                });

        mapper.updateEntityFromDTO(request, user);

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        return mapper.toResponse(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        userRepository.delete(findUserById(id));
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}