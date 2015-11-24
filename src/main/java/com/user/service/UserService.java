package com.user.service;

import com.user.persistence.entity.User;
import com.user.persistence.repository.UserRepository;
import com.user.util.Helper;
import com.user.web.dto.UserDto;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class UserService {

    @Value("${upload.path}")
    String uploadPath;
    @Autowired
    UserRepository userRepository;

    public static String getPathToImg(UserDto user) {
        return User.PATH_TO_PICTURES + "/" + user.getId() + "/" + user.getPhoto();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> findOne(String id) {
        return Optional.of(userRepository.findOne(id));
    }

    public void delete(User user) {
        deletePicture(user);
        userRepository.delete(user);
        log.debug("User {} was deleted", user.getId());
    }

    public String savePicture(User user, MultipartFile file) {
        if (Objects.isNull(user.getId())) {
            log.error("User id is null. Picture does not save.");
            return null;
        } else {
            String uniqueName = UUID.randomUUID().toString();
            Path path = Paths.get(uploadPath + "/" + User.PATH_TO_PICTURES + "/" + user.getId() + "/" +
                    uniqueName + "." + Helper.getFileExtension(file.getOriginalFilename()));
            try {
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());
                log.info("Image saved to {}", path);
                return path.getFileName().toString();
            } catch (IOException e) {
                log.error("Image does not saved. {}", e.getMessage());
            }
            return null;
        }
    }

    public void deletePicture(User user) {
        Path path = Paths.get(uploadPath + "/" + User.PATH_TO_PICTURES + "/" + user.getId());
        if (Files.exists(path)) {
            try {
                Helper.deleteFileRecursive(path);
                log.debug("User picture {} was deleted", user.getId());
            } catch (IOException e) {
                log.error("User image does not deleted. {}", e.getMessage());
            }
        } else {
            log.debug("User picture director does not exists");
        }
    }
}
