package com.user.service;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.user.util.Helper;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

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
    @Autowired
    GridFsTemplate gridFsTemplate;

    public GridFSFile store(InputStream content, String filename, String contentType, Object metadata) {
        return gridFsTemplate.store(content, filename, contentType, metadata);
    }

    public GridFSFile storeWithRandomName(InputStream content, String originalFilename, String contentType, Object metadata) {
        String uniqueName = UUID.randomUUID().toString();
        String ext = Helper.getFileExtension(originalFilename);
        return store(content, uniqueName + "." + ext, contentType, metadata);
    }

    public void delete(String id) {
        gridFsTemplate.delete(Query.query(GridFsCriteria.where("_id").is(id)));
    }

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

    private void resizeThumbnailator(InputStream inputStream, Path output, Integer width, Integer height) throws IOException {
        Thumbnails.of(inputStream).size(width, height).toFile(output.toFile());
    }

    public Path generate(String param, String entity, String id, String name) throws IOException {
        Path cachePath = Paths.get(mediaCachePath + param + "/" + entity + "/" + id + "/" + name);
        if (Files.exists(cachePath)) {
            return cachePath;
        }
        Integer width = environment.getProperty(param + ".width", Integer.class);
        Integer height = environment.getProperty(param + ".height", Integer.class);
        if (isNull(width) || isNull(height)) {
            log.error("Image cache was not generated. Param '{}' not found", param);
            return null;
        }
        GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(name)));
        if (isNull(file)) {
            log.error("Image cache was not generated. Image not found", uploadPath);
            return null;
        }
        Files.createDirectories(cachePath.getParent());
        log.debug("Created cache dir {}", cachePath.toString());
        resizeThumbnailator(file.getInputStream(), cachePath, width, height);
        return cachePath;
    }

    public void cacheClear() throws IOException {
        Helper.deleteFileRecursive(Paths.get(mediaCachePath));
        log.info("Media cache was removed");
    }

}
