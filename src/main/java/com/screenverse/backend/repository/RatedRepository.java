package com.screenverse.backend.repository;

import com.screenverse.backend.domain.rated.Rated;
import com.screenverse.backend.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing Rated entities
 */
@Repository
public interface RatedRepository extends JpaRepository<Rated, Long> {
    
    /**
     * Find all rated items for a specific user
     * 
     * @param user The user whose rated items to find
     * @return A list of rated items for the user
     */
    List<Rated> findByUser(Users user);
    
    /**
     * Find a specific rated item by user and title ID
     * 
     * @param user The user who rated the title
     * @param titleId The ID of the title
     * @return An Optional containing the rated item if found
     */
    Optional<Rated> findByUserAndTitleId(Users user, Integer titleId);
}