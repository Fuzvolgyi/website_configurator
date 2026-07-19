CREATE TABLE site_images (
    id BIGSERIAL PRIMARY KEY,
    image_key VARCHAR(255) NOT NULL,
    storage_url TEXT NOT NULL,
    site_id BIGINT NOT NULL REFERENCES sites(id) ON DELETE CASCADE,
    UNIQUE(image_key, site_id)
);
