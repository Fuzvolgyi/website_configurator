-- Home page banners.
--
-- A banner owns no text or image of its own: it holds a key prefix, and the actual
-- content lives in the existing texts and site_images tables under <banner_key>.title,
-- <banner_key>.subtitle and <banner_key>.background. That keeps the existing
-- translation and image upload machinery working untouched, and means a banner is
-- editable with the same admin tooling as everything else.
CREATE TABLE banners (
    id BIGSERIAL PRIMARY KEY,
    banner_key VARCHAR(255) NOT NULL,
    position INTEGER NOT NULL,
    visible BOOLEAN NOT NULL DEFAULT TRUE,
    site_id BIGINT NOT NULL REFERENCES sites(id) ON DELETE CASCADE,
    UNIQUE(banner_key, site_id)
);

-- How long each banner stays on screen, in seconds. Per site rather than per banner:
-- a carousel with different dwell times per slide reads as broken, not as a feature.
ALTER TABLE sites ADD COLUMN banner_interval_seconds INTEGER NOT NULL DEFAULT 8;

-- Carry the existing single hero over as the first banner, so the page looks the same
-- the moment this ships and the editor has something to work from.
INSERT INTO banners (banner_key, position, visible, site_id)
SELECT 'home.hero', 0, TRUE, id FROM sites;
