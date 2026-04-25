package com.project.ecommerce.domain.auth.service;

import com.project.ecommerce.domain.auth.dto.request.LoginRequestDTO;
import com.project.ecommerce.domain.auth.dto.response.AuthResponseDTO;
import com.project.ecommerce.domain.user.dto.request.CreateUserRequestDTO;
import com.project.ecommerce.domain.user.entity.User;
import com.project.ecommerce.domain.user.mapper.UserMapper;
import com.project.ecommerce.domain.user.repository.UserRepository;
import com.project.ecommerce.infra.exception.AlreadyExistsException;
import com.project.ecommerce.infra.security.service.JwtService;
import com.project.ecommerce.infra.security.service.UserDetailsImpl;
import com.project.ecommerce.shared.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthResponseDTO register(CreateUserRequestDTO request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new AlreadyExistsException("Email already registered");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER); // sempre USER no registro público

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(new UserDetailsImpl(saved));

        return new AuthResponseDTO(token, saved.getName(), saved.getEmail(), saved.getRole().name());
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(new UserDetailsImpl(user));

        return new AuthResponseDTO(token, user.getName(), user.getEmail(), user.getRole().name());
    }
}