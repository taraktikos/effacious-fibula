package com.user.service;

import com.user.persistence.entity.Article;
import com.user.persistence.repository.ArticleRepository;
import com.user.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;

@Slf4j
@Component
public class ArticleService {

    @Value("${upload.path}")
    String uploadPath;
    @Autowired
    ArticleRepository articleRepository;

//    public static String getPathToImg(ArticleDto article) {
//        return Article.PATH_TO_PICTURES + "/" + article.getId() + "/" + article.getPhoto();
//    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    public Optional<Article> findOne(String id) {
        return Optional.of(articleRepository.findOne(id));
    }

    public void delete(Article article) {
        deletePicture(article);
        articleRepository.delete(article);
        log.debug("Article {} was deleted", article.getId());
    }

    public String savePicture(Article article, MultipartFile file) {
        if (isNull(article.getId())) {
            log.error("Article id is null. Picture does not save.");
            return null;
        } else {
            String uniqueName = UUID.randomUUID().toString();
            Path path = Paths.get(uploadPath + "/" + Article.PATH_TO_PICTURES + "/" + article.getId() + "/" +
                    uniqueName + "." + Helper.getFileExtension(file.getOriginalFilename()));
            try {
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());
                log.debug("Image saved to {}", path);
                return path.getFileName().toString();
            } catch (IOException e) {
                log.error("Image does not saved. {}", e.getMessage());
            }
            return null;
        }
    }

    public void deletePicture(Article article) {
        Path path = Paths.get(uploadPath + "/" + Article.PATH_TO_PICTURES + "/" + article.getId());
        if (Files.exists(path)) {
            try {
                Helper.deleteFileRecursive(path);
                log.debug("Article picture {} was deleted", article.getId());
            } catch (IOException e) {
                log.error("Article image does not deleted. {}", e.getMessage());
            }
        } else {
            log.debug("Article picture director does not exists");
        }
    }

}
