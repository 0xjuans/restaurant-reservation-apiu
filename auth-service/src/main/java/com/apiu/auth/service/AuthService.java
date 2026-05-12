package com.apiu.auth.service;

import com.apiu.auth.dto.request.LoginRequest;
import com.apiu.auth.dto.request.RegisterRequest;
import com.apiu.auth.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    void logout(String token);
}
