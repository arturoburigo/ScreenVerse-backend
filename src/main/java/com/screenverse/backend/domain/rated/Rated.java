package com.screenverse.backend.domain.rated;

import com.screenverse.backend.domain.users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a rated title by a user
 * Contains information about the title, whether it has been watched, and the user's rating
 */
@Table(name = "rated")
@Entity(name = "Rated")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Rated {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private Integer titleId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean watched = true;

    @Column(nullable = false)
    private Float rating;

    @Column(columnDefinition = "TEXT")
    private String plotOverview;

    private Integer year;
    
    private String type;
    
    private String genreName;
    
    private String poster;
}