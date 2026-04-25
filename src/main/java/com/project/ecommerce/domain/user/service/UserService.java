package com.project.ecommerce.domain.user.service;

import com.project.ecommerce.domain.user.dto.request.RegisterUserRequestDTO;
import com.project.ecommerce.domain.user.dto.response.RegisterUserResponseDTO;
import com.project.ecommerce.domain.user.entity.User;
import com.project.ecommerce.domain.user.mapper.UserMapper;
import com.project.ecommerce.domain.user.repository.UserRepository;
import com.project.ecommerce.shared.enums.Role;
import com.project.ecommerce.infra.exception.AlreadyExistsException;
import com.project.ecommerce.infra.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    private static final Role DEFAULT_ROLE = Role.USER;
    private static final Role ADMIN = Role.ADMIN;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper mapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterUserResponseDTO createUser(RegisterUserRequestDTO request) {

        if (userRepository.findByEmail(request.email()).isPresent()){
                    throw new AlreadyExistsException("Email already registered");
                }

        User user = mapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));

        if(user.getRole() == null || user.getRole() != ADMIN){
            user.setRole(DEFAULT_ROLE);
        }

        User saved = userRepository.save(user);

        return mapper.toRegisterResponse(saved);
    }
    
    public List<RegisterUserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(mapper::toRegisterResponse)
                .toList();
    }

    @Override
    public RegisterUserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return mapper.toRegisterResponse(user);
    }

    @Override
    public RegisterUserResponseDTO updateUser(UUID id, RegisterUserRequestDTO userDto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        userRepository.findByEmail(userDto.email())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new AlreadyExistsException("User already registered");
                });

        user.setName(userDto.name());
        user.setEmail(userDto.email());

        if (userDto.password() != null && !userDto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDto.password()));
        }
        User updated = userRepository.save(user);

        return mapper.toRegisterResponse(updated);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }
}