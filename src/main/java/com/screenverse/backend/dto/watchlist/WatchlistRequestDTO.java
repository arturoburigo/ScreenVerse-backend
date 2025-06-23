package com.screenverse.backend.dto.watchlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or updating a watchlist item
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistRequestDTO {
    private Integer titleId;
    private String name;
    private Boolean watched;
    private String plotOverview;
    private Integer year;
    private String type;
    private String genreName;
    private String poster;
}