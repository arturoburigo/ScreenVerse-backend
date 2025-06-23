package com.screenverse.backend.service.watchlist;

import com.screenverse.backend.domain.users.Users;
import com.screenverse.backend.domain.watchlist.Watchlist;
import com.screenverse.backend.dto.watchlist.WatchlistRequestDTO;
import com.screenverse.backend.dto.watchlist.WatchlistResponseDTO;
import com.screenverse.backend.repository.WatchlistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing watchlist items
 */
@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;

    public WatchlistService(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    /**
     * Get all watchlist items for a user
     *
     * @param user The user whose watchlist to retrieve
     * @return A list of watchlist items
     */
    public List<WatchlistResponseDTO> getAllWatchlistItems(Users user) {
        return watchlistRepository.findByUser(user)
                .stream()
                .map(WatchlistResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Add a title to a user's watchlist
     *
     * @param user The user adding the title
     * @param request The watchlist item details
     * @return The created watchlist item
     */
    public WatchlistResponseDTO addToWatchlist(Users user, WatchlistRequestDTO request) {
        // Check if the title is already in the watchlist
        var existingItem = watchlistRepository.findByUserAndTitleId(user, request.getTitleId());
        if (existingItem.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Title already in watchlist");
        }

        var watchlistItem = new Watchlist();
        watchlistItem.setUser(user);
        watchlistItem.setTitleId(request.getTitleId());
        watchlistItem.setName(request.getName());
        watchlistItem.setWatched(request.getWatched() != null ? request.getWatched() : false);
        watchlistItem.setPlotOverview(request.getPlotOverview());
        watchlistItem.setYear(request.getYear());
        watchlistItem.setType(request.getType());
        watchlistItem.setGenreName(request.getGenreName());
        watchlistItem.setPoster(request.getPoster());

        watchlistItem = watchlistRepository.save(watchlistItem);
        return new WatchlistResponseDTO(watchlistItem);
    }

    /**
     * Update a watchlist item
     *
     * @param user The user who owns the watchlist item
     * @param id The ID of the watchlist item to update
     * @param request The updated watchlist item details
     * @return The updated watchlist item
     */
    public WatchlistResponseDTO updateWatchlistItem(Users user, Long id, WatchlistRequestDTO request) {
        var watchlistItem = watchlistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Watchlist item not found"));

        // Ensure the user owns the watchlist item
        if (!watchlistItem.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to update this watchlist item");
        }

        // Update the fields
        if (request.getName() != null) {
            watchlistItem.setName(request.getName());
        }
        if (request.getWatched() != null) {
            watchlistItem.setWatched(request.getWatched());
        }
        if (request.getPlotOverview() != null) {
            watchlistItem.setPlotOverview(request.getPlotOverview());
        }
        if (request.getYear() != null) {
            watchlistItem.setYear(request.getYear());
        }
        if (request.getType() != null) {
            watchlistItem.setType(request.getType());
        }
        if (request.getGenreName() != null) {
            watchlistItem.setGenreName(request.getGenreName());
        }
        if (request.getPoster() != null) {
            watchlistItem.setPoster(request.getPoster());
        }

        watchlistItem = watchlistRepository.save(watchlistItem);
        return new WatchlistResponseDTO(watchlistItem);
    }

    /**
     * Delete a watchlist item
     *
     * @param user The user who owns the watchlist item
     * @param id The ID of the watchlist item to delete
     */
    public void deleteWatchlistItem(Users user, Long id) {
        var watchlistItem = watchlistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Watchlist item not found"));

        // Ensure the user owns the watchlist item
        if (!watchlistItem.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to delete this watchlist item");
        }

        watchlistRepository.delete(watchlistItem);
    }

    /**
     * Mark a watchlist item as watched or unwatched
     *
     * @param user The user who owns the watchlist item
     * @param id The ID of the watchlist item to update
     * @param watched Whether the item has been watched
     * @return The updated watchlist item
     */
    public WatchlistResponseDTO markAsWatched(Users user, Long id, Boolean watched) {
        var watchlistItem = watchlistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Watchlist item not found"));

        // Ensure the user owns the watchlist item
        if (!watchlistItem.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to update this watchlist item");
        }

        watchlistItem.setWatched(watched);
        watchlistItem = watchlistRepository.save(watchlistItem);
        return new WatchlistResponseDTO(watchlistItem);
    }
}