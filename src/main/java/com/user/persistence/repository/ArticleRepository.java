package com.user.persistence.repository;

import com.user.persistence.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ArticleRepository extends MongoRepository<Article, String> {
    Optional<Article> findByUri(String uri);
}
