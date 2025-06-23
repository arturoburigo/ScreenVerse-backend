CREATE TABLE IF NOT EXISTS watchlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    watched BOOLEAN DEFAULT FALSE,
    plot_overview TEXT,
    year INT,
    type VARCHAR(50),
    genre_name VARCHAR(255),
    poster VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_watchlist_user_id ON watchlist(user_id);
CREATE INDEX idx_watchlist_title_id ON watchlist(title_id);
CREATE UNIQUE INDEX idx_watchlist_user_title ON watchlist(user_id, title_id);