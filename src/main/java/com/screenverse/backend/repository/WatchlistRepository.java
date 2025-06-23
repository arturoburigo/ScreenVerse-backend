package com.screenverse.backend.repository;

import com.screenverse.backend.domain.users.Users;
import com.screenverse.backend.domain.watchlist.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing Watchlist entities
 */
@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    
    /**
     * Find all watchlist items for a specific user
     * 
     * @param user The user whose watchlist items to find
     * @return A list of watchlist items for the user
     */
    List<Watchlist> findByUser(Users user);
    
    /**
     * Find a specific watchlist item by user and title ID
     * 
     * @param user The user who owns the watchlist item
     * @param titleId The ID of the title
     * @return An Optional containing the watchlist item if found
     */
    Optional<Watchlist> findByUserAndTitleId(Users user, Integer titleId);
}