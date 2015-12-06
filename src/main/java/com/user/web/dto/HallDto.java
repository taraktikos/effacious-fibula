package com.user.web.dto;

import com.user.validation.constraints.FileSize;
import com.user.validation.constraints.FileType;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.Objects.isNull;

@Data
public class HallDto {

    private String id;
    @NotEmpty
    private String title;
    private String uri;
    private String description;
    private String address;
    private String organizationName;
    private String nearbyMetroStation;
    private GeoJsonPoint location;
    private Double hourlyRate;
    private Double dailyRate;
    private Integer minSeatsNumber;
    private Integer maxSeatsNumber;
    private Integer square;
    private Integer phone;
    private boolean active;
    private Integer ordinalNumber;

    public Boolean isNew() {
        return isNull(id);
    }
}
