package com.screenverse.backend.dto.watchlist;

import com.screenverse.backend.domain.watchlist.Watchlist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for returning watchlist item information to the client
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistResponseDTO {
    private Long id;
    private Integer titleId;
    private String name;
    private Boolean watched;
    private String plotOverview;
    private Integer year;
    private String type;
    private String genreName;
    private String poster;
    
    public WatchlistResponseDTO(Watchlist watchlist) {
        this.id = watchlist.getId();
        this.titleId = watchlist.getTitleId();
        this.name = watchlist.getName();
        this.watched = watchlist.getWatched();
        this.plotOverview = watchlist.getPlotOverview();
        this.year = watchlist.getYear();
        this.type = watchlist.getType();
        this.genreName = watchlist.getGenreName();
        this.poster = watchlist.getPoster();
    }
}