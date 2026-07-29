CREATE TABLE urls (
        id BIGSERIAL PRIMARY KEY,
        original_url TEXT NOT NULL,
        short_code VARCHAR(20) NOT NULL UNIQUE,
        click_count BIGINT NOT NULL DEFAULT 0,
        active BOOLEAN NOT NULL DEFAULT TRUE,
        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        user_id BIGINT NOT NULL,
        CONSTRAINT fk_urls_user
            FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE
);