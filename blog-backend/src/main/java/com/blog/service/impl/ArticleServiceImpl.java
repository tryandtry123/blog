package com.blog.service.impl;

import com.blog.model.dto.ArticleDTO;
import com.blog.model.dto.ArticleRequest;
import com.blog.model.dto.UserDTO;
import com.blog.model.entity.Article;
import com.blog.repository.ArticleRepository;
import com.blog.repository.UserRepository;
import com.blog.service.ArticleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ArticleDTO createArticle(ArticleRequest request, String username) {
        log.info("Creating article for user: {}", username);
        var author = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        var article = new Article();
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setAuthor(author);

        var savedArticle = articleRepository.save(article);
        log.info("Article created with id: {}", savedArticle.getId());
        return convertToDTO(savedArticle);
    }

    @Override
    public ArticleDTO getArticle(Long id) {
        log.info("Fetching article with id: {}", id);
        var article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("文章不存在"));
        return convertToDTO(article);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleDTO> getAllArticles(Pageable pageable) {
        log.info("Fetching all articles with pageable: {}", pageable);
        return articleRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleDTO> getArticlesByAuthor(String username, Pageable pageable) {
        log.info("Fetching articles for user: {}", username);
        var author = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));
        return articleRepository.findByAuthorOrderByCreatedAtDesc(author, pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional
    public ArticleDTO updateArticle(Long id, ArticleRequest request, String username) {
        log.info("Updating article {} for user: {}", id, username);
        var article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("文章不存在"));

        if (!article.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("没有权限修改此文章");
        }

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());

        var updatedArticle = articleRepository.save(article);
        log.info("Article updated successfully");
        return convertToDTO(updatedArticle);
    }

    @Override
    @Transactional
    public void deleteArticle(Long id, String username) {
        log.info("Deleting article {} for user: {}", id, username);
        var article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("文章不存在"));

        if (!article.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("没有权限删除此文章");
        }

        articleRepository.delete(article);
        log.info("Article deleted successfully");
    }

    private ArticleDTO convertToDTO(Article article) {
        var dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        dto.setCreatedAt(article.getCreatedAt());
        dto.setUpdatedAt(article.getUpdatedAt());

        var authorDTO = new UserDTO();
        authorDTO.setId(article.getAuthor().getId());
        authorDTO.setUsername(article.getAuthor().getUsername());
        authorDTO.setEmail(article.getAuthor().getEmail());
        authorDTO.setRole(article.getAuthor().getRole());
        
        dto.setAuthor(authorDTO);
        return dto;
    }
} 