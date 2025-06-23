package com.screenverse.backend.dto.rated;

import com.screenverse.backend.domain.rated.Rated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for returning rated item information to the client
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatedResponseDTO {
    private Long id;
    private Integer titleId;
    private String name;
    private Boolean watched;
    private Float rating;
    private String plotOverview;
    private Integer year;
    private String type;
    private String genreName;
    private String poster;
    
    public RatedResponseDTO(Rated rated) {
        this.id = rated.getId();
        this.titleId = rated.getTitleId();
        this.name = rated.getName();
        this.watched = rated.getWatched();
        this.rating = rated.getRating();
        this.plotOverview = rated.getPlotOverview();
        this.year = rated.getYear();
        this.type = rated.getType();
        this.genreName = rated.getGenreName();
        this.poster = rated.getPoster();
    }
}