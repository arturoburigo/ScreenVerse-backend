package com.screenverse.backend.dto.rated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or updating a rated item
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatedRequestDTO {
    private Integer titleId;
    private String name;
    private Boolean watched;
    private Float rating;
    private String plotOverview;
    private Integer year;
    private String type;
    private String genreName;
    private String poster;
}