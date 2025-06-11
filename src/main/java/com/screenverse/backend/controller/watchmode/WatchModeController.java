package com.screenverse.backend.controller.watchmode;

import com.screenverse.backend.dto.watchmode.SearchResponseDTO;
import com.screenverse.backend.dto.watchmode.TitleDetailsDTO;
import com.screenverse.backend.service.watchmode.WatchModeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for interacting with the WatchMode API
 * This controller provides endpoints for searching titles and retrieving detailed information about specific titles
 */
@RestController
@RequestMapping("/api/watchmode")
public class WatchModeController {

    private final WatchModeService watchModeService;

    public WatchModeController(WatchModeService watchModeService) {
        this.watchModeService = watchModeService;
    }

    /**
     * Search for titles in the WatchMode API
     * 
     * @param searchValue The value to search for (e.g., "Breaking Bad", "American Pie")
     * @param searchField The field to search in (default is "name")
     * @return A response containing a list of titles matching the search criteria
     */
    @GetMapping("/search")
    public ResponseEntity<SearchResponseDTO> searchTitles(
            @RequestParam String searchValue,
            @RequestParam(defaultValue = "name") String searchField) {

        SearchResponseDTO response = watchModeService.searchTitles(searchValue, searchField);
        return ResponseEntity.ok(response);
    }

    /**
     * Get detailed information about a specific title from the WatchMode API
     * 
     * @param titleId The ID of the title to retrieve details for
     * @param region The region code to filter sources by (e.g., "US", "BR")
     * @param language The language code for localized content (e.g., "en", "pt")
     * @param appendToResponse Additional data to include in the response (e.g., "sources")
     * @return Detailed information about the requested title
     */
    @GetMapping("/title/{titleId}")
    public ResponseEntity<TitleDetailsDTO> getTitleDetails(
            @PathVariable Integer titleId,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String language,
            @RequestParam(required = false, name = "append_to_response") String appendToResponse) {

        TitleDetailsDTO response = watchModeService.getTitleDetails(titleId, region, language, appendToResponse);
        return ResponseEntity.ok(response);
    }
}
