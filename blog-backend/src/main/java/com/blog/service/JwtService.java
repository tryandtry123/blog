package com.blog.service;

import com.blog.model.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(User user);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
} 