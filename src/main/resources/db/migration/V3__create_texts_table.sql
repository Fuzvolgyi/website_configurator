CREATE TABLE texts (
    id BIGSERIAL PRIMARY KEY,
    translation_key VARCHAR(255) NOT NULL,
    content_value TEXT NOT NULL,
    site_id BIGINT NOT NULL REFERENCES sites(id) ON DELETE CASCADE,
    UNIQUE(translation_key, site_id)
);
