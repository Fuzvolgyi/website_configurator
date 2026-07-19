ALTER TABLE texts ADD COLUMN language VARCHAR(10) NOT NULL DEFAULT 'en';

ALTER TABLE texts DROP CONSTRAINT texts_translation_key_site_id_key;

ALTER TABLE texts ADD CONSTRAINT texts_translation_key_site_id_language_key UNIQUE(translation_key, site_id, language);
