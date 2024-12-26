package com.blog.model.dto;

import com.blog.model.entity.UserRole;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
} 