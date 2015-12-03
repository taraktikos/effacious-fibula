package com.user.web.dto;

import com.user.validation.constraints.FileType;
import com.user.validation.constraints.FileSize;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

import static java.util.Objects.isNull;

@Data
public class UserDto {
    private String id;
    @FileSize(value = 3, message = "Максимальний розмір файлу 3Мб")
    @FileType({"image/jpeg", "image/png"})
    private MultipartFile photoVirtual;
    private String photo;
    @NotEmpty
    private String email;
    @NotEmpty
    private String firstName;
    private String lastName;
    private String password;
    @NotEmpty
    private Set<String> roles;

    public Boolean isNew() {
        return isNull(id);
    }
}
