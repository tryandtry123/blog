package com.blog.controller;

import com.blog.model.dto.ArticleDTO;
import com.blog.model.dto.ArticleRequest;
import com.blog.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(
            @Valid @RequestBody ArticleRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            log.info("Creating article with title: {}", request.getTitle());
            ArticleDTO article = articleService.createArticle(request, userDetails.getUsername());
            log.info("Article created successfully with id: {}", article.getId());
            return ResponseEntity.ok(article);
        } catch (Exception e) {
            log.error("Error creating article: ", e);
            throw new RuntimeException("创建文章失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable Long id) {
        log.info("Fetching article with id: {}", id);
        return ResponseEntity.ok(articleService.getArticle(id));
    }

    @GetMapping
    public ResponseEntity<Page<ArticleDTO>> getAllArticles(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        log.info("Fetching all articles with pageable: {}", pageable);
        Page<ArticleDTO> articles = articleService.getAllArticles(pageable);
        log.info("Found {} articles", articles.getTotalElements());
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Page<ArticleDTO>> getArticlesByAuthor(
            @PathVariable String username,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        log.info("Fetching articles for user: {}", username);
        return ResponseEntity.ok(articleService.getArticlesByAuthor(username, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody ArticleRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Updating article with id: {}", id);
        return ResponseEntity.ok(articleService.updateArticle(id, request, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Deleting article with id: {}", id);
        articleService.deleteArticle(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
} 