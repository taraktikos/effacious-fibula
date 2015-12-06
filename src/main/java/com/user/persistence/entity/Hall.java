package com.user.persistence.entity;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Hall {

    public static final String PATH_TO_PICTURES = "halls";

    @Id
    private String id;
    private String title;
    @Indexed(unique = true)
    private String uri;
    private String description;
    private String address;
    private String organizationName;
    private String nearbyMetroStation;
    private GeoJsonPoint location;
    @DBRef
    private City city;
    private List<Image> images = Lists.newArrayList();
    private Double hourlyRate;
    private Double dailyRate;
    private Integer minSeatsNumber;
    private Integer maxSeatsNumber;
    private Integer square;
    private Integer phone;
    private boolean active;
    private Integer ordinalNumber;
    @CreatedDate
    private DateTime createdAt;
    @LastModifiedDate
    private DateTime updatedAt;

    @Data
    @Builder
    public static class Image {
        private ObjectId id;
        private String name;
        private boolean main;
    }
}
