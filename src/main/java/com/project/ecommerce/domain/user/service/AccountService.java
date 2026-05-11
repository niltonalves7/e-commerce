package com.project.ecommerce.domain.user.service;

import com.project.ecommerce.domain.user.dto.request.UpdatePasswordRequestDTO;
import com.project.ecommerce.domain.user.dto.request.UpdateProfileRequestDTO;
import com.project.ecommerce.domain.user.dto.response.UserResponseDTO;
import com.project.ecommerce.domain.user.entity.User;
import com.project.ecommerce.domain.user.mapper.UserMapper;
import com.project.ecommerce.domain.user.repository.UserRepository;
import com.project.ecommerce.infra.exception.AlreadyExistsException;
import com.project.ecommerce.infra.exception.BusinessException;
import com.project.ecommerce.infra.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO getAccountProfile() {
        return mapper.toResponse(getAuthenticatedUser());
    }

    public UserResponseDTO updateAccountProfile(UpdateProfileRequestDTO request) {
        User user = getAuthenticatedUser();

        userRepository.findUserByEmail(request.email())
                .filter(existing -> !existing.getId().equals(user.getId()))
                .ifPresent(existing -> {
                    throw new AlreadyExistsException("Email already in use");
                });

        user.setName(request.name());
        user.setEmail(request.email());

        return mapper.toResponse(userRepository.save(user));
    }

    public void updateAccountPassword(UpdatePasswordRequestDTO request) {
        User user = getAuthenticatedUser();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BusinessException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}