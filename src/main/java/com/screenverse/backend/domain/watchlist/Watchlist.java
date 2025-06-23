package com.screenverse.backend.domain.watchlist;

import com.screenverse.backend.domain.users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a title in a user's watchlist
 * Contains information about the title and whether it has been watched
 */
@Table(name = "watchlist")
@Entity(name = "Watchlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Watchlist {
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
    private Boolean watched = false;

    @Column(columnDefinition = "TEXT")
    private String plotOverview;

    private Integer year;
    
    private String type;
    
    private String genreName;
    
    private String poster;
}