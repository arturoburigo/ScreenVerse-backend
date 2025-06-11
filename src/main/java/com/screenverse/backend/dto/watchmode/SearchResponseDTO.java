package com.screenverse.backend.dto.watchmode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO representing the response from the WatchMode search API
 * Contains lists of title results and people results matching the search criteria
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDTO {
    private List<SearchResultDTO> title_results;
    private List<SearchResultDTO> people_results;
}
