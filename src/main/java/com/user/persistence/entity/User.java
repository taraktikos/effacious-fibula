package com.user.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class User {

    public static final String PATH_TO_PICTURES = "avatars";

    @Id
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String role;
    private String photo;

    public enum Roles {
        ROLE_ADMIN, ROLE_USER
    }

}
