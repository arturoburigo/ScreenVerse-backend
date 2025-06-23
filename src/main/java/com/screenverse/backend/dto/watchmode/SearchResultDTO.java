package com.screenverse.backend.dto.watchmode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a search result from the WatchMode API
 * Contains basic information about a title or person
 * Can also include detailed information if available
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDTO {
    private String resultType;
    private Integer id;
    private String name;
    private String type;
    private Integer year;
    private String imdb_id;
    private Integer tmdb_id;
    private String tmdb_type;

    // Detailed information about the title, populated for the first 5 results
    private TitleDetailsDTO details;
}
