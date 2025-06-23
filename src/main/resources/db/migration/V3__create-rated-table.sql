CREATE TABLE IF NOT EXISTS rated (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    watched BOOLEAN DEFAULT TRUE,
    rating FLOAT NOT NULL,
    plot_overview TEXT,
    year INT,
    type VARCHAR(50),
    genre_name VARCHAR(255),
    poster VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CHECK (rating >= 1.0 AND rating <= 5.0)
);

CREATE INDEX idx_rated_user_id ON rated(user_id);
CREATE INDEX idx_rated_title_id ON rated(title_id);
CREATE UNIQUE INDEX idx_rated_user_title ON rated(user_id, title_id);