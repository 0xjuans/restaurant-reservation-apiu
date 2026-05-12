package com.apiu.auth.dto.response;

import java.util.Set;

public record AuthResponse(
        Long userId,
        String email,
        Set<String> roles,
        String token,
        long expiresIn
) {}
