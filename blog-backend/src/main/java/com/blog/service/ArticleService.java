package com.blog.service;

import com.blog.model.dto.ArticleDTO;
import com.blog.model.dto.ArticleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    ArticleDTO createArticle(ArticleRequest request, String username);
    ArticleDTO getArticle(Long id);
    Page<ArticleDTO> getAllArticles(Pageable pageable);
    Page<ArticleDTO> getArticlesByAuthor(String username, Pageable pageable);
    ArticleDTO updateArticle(Long id, ArticleRequest request, String username);
    void deleteArticle(Long id, String username);
} 