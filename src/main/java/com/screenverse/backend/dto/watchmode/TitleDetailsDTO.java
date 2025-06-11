package com.screenverse.backend.dto.watchmode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO representing detailed information about a title from the WatchMode API
 * Contains comprehensive information about a movie or TV show, including metadata, ratings, and streaming sources
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TitleDetailsDTO {
    private Integer id;
    private String title;
    private String original_title;
    private String plot_overview;
    private String type;
    private Integer runtime_minutes;
    private Integer year;
    private Integer end_year;
    private String release_date;
    private String imdb_id;
    private Integer tmdb_id;
    private String tmdb_type;
    private List<Integer> genres;
    private List<String> genre_names;
    private Double user_rating;
    private Integer critic_score;
    private String us_rating;
    private String poster;
    private String posterMedium;
    private String posterLarge;
    private String backdrop;
    private String original_language;
    private List<Integer> similar_titles;
    private List<Integer> networks;
    private List<String> network_names;
    private Double relevance_percentile;
    private Double popularity_percentile;
    private String trailer;
    private String trailer_thumbnail;
    private String english_title;
    private List<SourceDTO> sources;
}
