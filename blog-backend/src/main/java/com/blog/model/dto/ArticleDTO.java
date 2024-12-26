package com.blog.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ArticleDTO {
    private Long id;
    private String title;
    private String content;
    private UserDTO author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 