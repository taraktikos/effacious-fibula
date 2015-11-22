package com.user.web.dto;

import com.user.validation.constraints.File;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import static java.util.Objects.isNull;

@Data
public class UserDto {
    private String id;
    @File(contentType = {"image/jpeg", "image/png"}, maxSize = "3Mb")
    private MultipartFile photoVirtual;
    private String photo;
    @NotEmpty
    private String email;
    @NotEmpty
    private String firstName;
    private String lastName;
    private String password;
    private String role;

    public Boolean isNew() {
        return isNull(id);
    }
}
