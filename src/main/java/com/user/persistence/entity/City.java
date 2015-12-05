package com.user.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    private String id;
    private String name;
    private String nameLocative;
    private String namePossessive;
    @Indexed(unique = true)
    private String uri;
    private boolean active;
    @DBRef
    private List<Hall> halls;

}
