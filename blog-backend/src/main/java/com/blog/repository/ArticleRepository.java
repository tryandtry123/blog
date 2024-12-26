package com.blog.repository;

import com.blog.model.entity.Article;
import com.blog.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Article> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);
} 