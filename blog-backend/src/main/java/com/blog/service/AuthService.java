package com.blog.service;

import com.blog.model.dto.AuthResponse;
import com.blog.model.dto.LoginRequest;
import com.blog.model.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
} 