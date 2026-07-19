CREATE TABLE pictures (
    id BIGSERIAL PRIMARY KEY,
    storage_url VARCHAR(2048) NOT NULL,
    alt_text VARCHAR(255) NOT NULL,
    display_order INTEGER NOT NULL,
    site_id BIGINT NOT NULL REFERENCES sites(id) ON DELETE CASCADE
);
