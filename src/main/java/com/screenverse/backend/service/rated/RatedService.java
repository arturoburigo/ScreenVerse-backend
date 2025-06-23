package com.screenverse.backend.service.rated;

import com.screenverse.backend.domain.rated.Rated;
import com.screenverse.backend.domain.users.Users;
import com.screenverse.backend.dto.rated.RatedRequestDTO;
import com.screenverse.backend.dto.rated.RatedResponseDTO;
import com.screenverse.backend.repository.RatedRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing rated items
 */
@Service
public class RatedService {

    private final RatedRepository ratedRepository;

    public RatedService(RatedRepository ratedRepository) {
        this.ratedRepository = ratedRepository;
    }

    /**
     * Get all rated items for a user
     *
     * @param user The user whose rated items to retrieve
     * @return A list of rated items
     */
    public List<RatedResponseDTO> getAllRatedItems(Users user) {
        return ratedRepository.findByUser(user)
                .stream()
                .map(RatedResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Rate a title
     *
     * @param user The user rating the title
     * @param request The rated item details
     * @return The created rated item
     */
    public RatedResponseDTO rateTitle(Users user, RatedRequestDTO request) {
        // Validate rating
        if (request.getRating() == null || request.getRating() < 1.0f || request.getRating() > 5.0f) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        // Check if the title is already rated by the user
        var existingItem = ratedRepository.findByUserAndTitleId(user, request.getTitleId());
        
        Rated ratedItem;
        if (existingItem.isPresent()) {
            // Update existing rating
            ratedItem = existingItem.get();
            ratedItem.setRating(request.getRating());
            ratedItem.setWatched(request.getWatched() != null ? request.getWatched() : true);
            
            // Update other fields if provided
            if (request.getName() != null) {
                ratedItem.setName(request.getName());
            }
            if (request.getPlotOverview() != null) {
                ratedItem.setPlotOverview(request.getPlotOverview());
            }
            if (request.getYear() != null) {
                ratedItem.setYear(request.getYear());
            }
            if (request.getType() != null) {
                ratedItem.setType(request.getType());
            }
            if (request.getGenreName() != null) {
                ratedItem.setGenreName(request.getGenreName());
            }
            if (request.getPoster() != null) {
                ratedItem.setPoster(request.getPoster());
            }
        } else {
            // Create new rating
            ratedItem = new Rated();
            ratedItem.setUser(user);
            ratedItem.setTitleId(request.getTitleId());
            ratedItem.setName(request.getName());
            ratedItem.setRating(request.getRating());
            ratedItem.setWatched(request.getWatched() != null ? request.getWatched() : true);
            ratedItem.setPlotOverview(request.getPlotOverview());
            ratedItem.setYear(request.getYear());
            ratedItem.setType(request.getType());
            ratedItem.setGenreName(request.getGenreName());
            ratedItem.setPoster(request.getPoster());
        }

        ratedItem = ratedRepository.save(ratedItem);
        return new RatedResponseDTO(ratedItem);
    }

    /**
     * Update a rated item
     *
     * @param user The user who rated the title
     * @param id The ID of the rated item to update
     * @param request The updated rated item details
     * @return The updated rated item
     */
    public RatedResponseDTO updateRatedItem(Users user, Long id, RatedRequestDTO request) {
        var ratedItem = ratedRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rated item not found"));

        // Ensure the user owns the rated item
        if (!ratedItem.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to update this rated item");
        }

        // Validate rating if provided
        if (request.getRating() != null) {
            if (request.getRating() < 1.0f || request.getRating() > 5.0f) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
            }
            ratedItem.setRating(request.getRating());
        }

        // Update other fields if provided
        if (request.getName() != null) {
            ratedItem.setName(request.getName());
        }
        if (request.getWatched() != null) {
            ratedItem.setWatched(request.getWatched());
        }
        if (request.getPlotOverview() != null) {
            ratedItem.setPlotOverview(request.getPlotOverview());
        }
        if (request.getYear() != null) {
            ratedItem.setYear(request.getYear());
        }
        if (request.getType() != null) {
            ratedItem.setType(request.getType());
        }
        if (request.getGenreName() != null) {
            ratedItem.setGenreName(request.getGenreName());
        }
        if (request.getPoster() != null) {
            ratedItem.setPoster(request.getPoster());
        }

        ratedItem = ratedRepository.save(ratedItem);
        return new RatedResponseDTO(ratedItem);
    }

    /**
     * Delete a rated item
     *
     * @param user The user who rated the title
     * @param id The ID of the rated item to delete
     */
    public void deleteRatedItem(Users user, Long id) {
        var ratedItem = ratedRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rated item not found"));

        // Ensure the user owns the rated item
        if (!ratedItem.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to delete this rated item");
        }

        ratedRepository.delete(ratedItem);
    }
}