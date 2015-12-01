package com.user.web.dto;

import com.sun.istack.internal.NotNull;
import com.user.persistence.repository.ArticleRepository;
import com.user.service.ArticleService;
import com.user.validation.constraints.FileSize;
import com.user.validation.constraints.FileType;
import com.user.validation.constraints.Unique;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

import static java.util.Objects.isNull;

@Data
public class ArticleDto {

    private String id;
    @FileSize(value = 3, message = "Максимальний розмір файлу 3Мб")
    @FileType({"image/jpeg", "image/png"})
    private MultipartFile imageVirtual;
    private String image;
    @NotEmpty
    private String title;
    @NotEmpty
//    @Unique(field = "uri", service = ArticleService.class)
    private String uri;
    @NotEmpty
    private String description;
    @NotEmpty
    private String text;
//    @NotNull
    private boolean active;
    private DateTime createdAt;

    public Boolean isNew() {
        return isNull(id);
    }
}
