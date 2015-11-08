package com.user.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@Document
public class User {

    @Id
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private List<Roles> roles;

    public enum Roles {
        ROLE_ADMIN, ROLE_RETAILER, ROLE_USER
    }

}
