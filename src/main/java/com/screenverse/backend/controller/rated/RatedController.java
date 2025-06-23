package com.screenverse.backend.controller.rated;

import com.screenverse.backend.domain.users.Users;
import com.screenverse.backend.dto.rated.RatedRequestDTO;
import com.screenverse.backend.dto.rated.RatedResponseDTO;
import com.screenverse.backend.service.rated.RatedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing rated items
 */
@RestController
@RequestMapping("/api/rated")
public class RatedController {

    private final RatedService ratedService;

    public RatedController(RatedService ratedService) {
        this.ratedService = ratedService;
    }

    /**
     * Get all rated items for the authenticated user
     *
     * @param user The authenticated user
     * @return A list of rated items
     */
    @GetMapping
    public ResponseEntity<List<RatedResponseDTO>> getAllRatedItems(@AuthenticationPrincipal Users user) {
        return ResponseEntity.ok(ratedService.getAllRatedItems(user));
    }

    /**
     * Rate a title
     *
     * @param user The authenticated user
     * @param request The rated item details
     * @return The created rated item
     */
    @PostMapping
    public ResponseEntity<RatedResponseDTO> rateTitle(
            @AuthenticationPrincipal Users user,
            @RequestBody RatedRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratedService.rateTitle(user, request));
    }

    /**
     * Update a rated item
     *
     * @param user The authenticated user
     * @param id The ID of the rated item to update
     * @param request The updated rated item details
     * @return The updated rated item
     */
    @PutMapping("/{id}")
    public ResponseEntity<RatedResponseDTO> updateRatedItem(
            @AuthenticationPrincipal Users user,
            @PathVariable Long id,
            @RequestBody RatedRequestDTO request) {
        return ResponseEntity.ok(ratedService.updateRatedItem(user, id, request));
    }

    /**
     * Delete a rated item
     *
     * @param user The authenticated user
     * @param id The ID of the rated item to delete
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRatedItem(
            @AuthenticationPrincipal Users user,
            @PathVariable Long id) {
        ratedService.deleteRatedItem(user, id);
        return ResponseEntity.noContent().build();
    }
}