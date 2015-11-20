package com.user.service;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.notExists;
import static java.util.Objects.isNull;

@Slf4j
@Component
public class MediaService {

    @Autowired
    Environment environment;
    @Value("${media.cache.path}")
    String mediaCachePath;
    @Value("${upload.path}")
    String uploadPath;

    public Path generate(String param, String path) throws IOException {
        Integer width = environment.getProperty(param + ".width", Integer.class);
        Integer height = environment.getProperty(param + ".height", Integer.class);
        if (isNull(width) || isNull(height)) {
            log.error("Image cache was not generated. Param '{}' not found", param);
            return null;
        }
        Path uploadPath = Paths.get(this.uploadPath + path);
        if (notExists(uploadPath)) {
            log.error("Image cache was not generated. Path '{}' not correct", uploadPath);
            return null;
        }

        Path cachePath = Paths.get(mediaCachePath + param + path);
        if (Files.exists(cachePath)) {
            return cachePath;
        }
        Files.createDirectories(cachePath.getParent());
        resizeThumbnailator(uploadPath, cachePath, width, height);
        return cachePath;
    }

    private void resizeThumbnailator(Path input, Path output, Integer width, Integer height) throws IOException {
        Thumbnails.of(input.toFile()).size(width, height).toFile(output.toFile());
    }

}
