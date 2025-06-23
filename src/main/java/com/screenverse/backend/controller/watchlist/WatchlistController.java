package com.screenverse.backend.controller.watchlist;

import com.screenverse.backend.domain.users.Users;
import com.screenverse.backend.dto.watchlist.WatchlistRequestDTO;
import com.screenverse.backend.dto.watchlist.WatchlistResponseDTO;
import com.screenverse.backend.service.watchlist.WatchlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing watchlist items
 */
@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    /**
     * Get all watchlist items for the authenticated user
     *
     * @param user The authenticated user
     * @return A list of watchlist items
     */
    @GetMapping
    public ResponseEntity<List<WatchlistResponseDTO>> getAllWatchlistItems(@AuthenticationPrincipal Users user) {
        return ResponseEntity.ok(watchlistService.getAllWatchlistItems(user));
    }

    /**
     * Add a title to the authenticated user's watchlist
     *
     * @param user The authenticated user
     * @param request The watchlist item details
     * @return The created watchlist item
     */
    @PostMapping
    public ResponseEntity<WatchlistResponseDTO> addToWatchlist(
            @AuthenticationPrincipal Users user,
            @RequestBody WatchlistRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(watchlistService.addToWatchlist(user, request));
    }

    /**
     * Update a watchlist item
     *
     * @param user The authenticated user
     * @param id The ID of the watchlist item to update
     * @param request The updated watchlist item details
     * @return The updated watchlist item
     */
    @PutMapping("/{id}")
    public ResponseEntity<WatchlistResponseDTO> updateWatchlistItem(
            @AuthenticationPrincipal Users user,
            @PathVariable Long id,
            @RequestBody WatchlistRequestDTO request) {
        return ResponseEntity.ok(watchlistService.updateWatchlistItem(user, id, request));
    }

    /**
     * Delete a watchlist item
     *
     * @param user The authenticated user
     * @param id The ID of the watchlist item to delete
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatchlistItem(
            @AuthenticationPrincipal Users user,
            @PathVariable Long id) {
        watchlistService.deleteWatchlistItem(user, id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Mark a watchlist item as watched or unwatched
     *
     * @param user The authenticated user
     * @param id The ID of the watchlist item to update
     * @param watched Whether the item has been watched
     * @return The updated watchlist item
     */
    @PatchMapping("/{id}/watched")
    public ResponseEntity<WatchlistResponseDTO> markAsWatched(
            @AuthenticationPrincipal Users user,
            @PathVariable Long id,
            @RequestParam Boolean watched) {
        return ResponseEntity.ok(watchlistService.markAsWatched(user, id, watched));
    }
}