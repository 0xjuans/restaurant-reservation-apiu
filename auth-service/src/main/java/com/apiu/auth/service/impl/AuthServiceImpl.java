package com.apiu.auth.service.impl;

import com.apiu.auth.dto.request.LoginRequest;
import com.apiu.auth.dto.request.RegisterRequest;
import com.apiu.auth.dto.response.AuthResponse;
import com.apiu.auth.entity.User;
import com.apiu.auth.enums.RoleName;
import com.apiu.auth.exception.ApiException;
import com.apiu.auth.repository.RoleRepository;
import com.apiu.auth.repository.UserRepository;
import com.apiu.auth.service.AuthService;
import com.apiu.auth.service.JwtService;
import com.apiu.auth.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ApiException("El email ya está registrado", HttpStatus.CONFLICT);
        }

        var customerRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> new ApiException("Rol no encontrado", HttpStatus.INTERNAL_SERVER_ERROR));

        var user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(customerRole))
                .build();

        var saved = userRepository.save(user);
        var userDetails = userDetailsService.loadUserByUsername(saved.getEmail());
        var token = jwtService.generateToken(userDetails, saved.getId());

        return buildAuthResponse(saved.getId(), userDetails, token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (BadCredentialsException ex) {
            throw new ApiException("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        }

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        var userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        var token = jwtService.generateToken(userDetails, user.getId());

        return buildAuthResponse(user.getId(), userDetails, token);
    }

    @Override
    public void logout(String token) {
        jwtService.blacklist(token);
    }

    private AuthResponse buildAuthResponse(Long userId, UserDetails userDetails, String token) {
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toSet());

        return new AuthResponse(
                userId,
                userDetails.getUsername(),
                roles,
                token,
                jwtService.getExpirationMs()
        );
    }
}
