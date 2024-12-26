package com.blog.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArticleRequest {
    @NotBlank(message = "标题不能为空")
    @Size(min = 2, max = 100, message = "标题长度必须在2-100个字符之间")
    private String title;
    
    @NotBlank(message = "内容不能为空")
    @Size(min = 1, message = "内容不能为空")
    private String content;
} 