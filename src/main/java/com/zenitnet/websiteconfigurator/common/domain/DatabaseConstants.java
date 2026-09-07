package com.zenitnet.websiteconfigurator.common.domain;

public final class DatabaseConstants {

    private DatabaseConstants() {}

    public static final class TableName {
        public static final String SITES = "sites";
        public static final String PICTURES = "pictures";
        public static final String TEXTS = "texts";
        public static final String SITE_IMAGES = "site_images";
        public static final String BANNERS = "banners";

        private TableName() {}
    }

    public static final class FieldName {

        private FieldName() {}

        public static final class Sites {
            public static final String NAME = "name";
            public static final String ROUTE_PATH = "route_path";
            public static final String ACTIVE = "active";
            public static final String BANNER_INTERVAL_SECONDS = "banner_interval_seconds";

            private Sites() {}
        }

        public static final class Pictures {
            public static final String STORAGE_URL = "storage_url";
            public static final String ALT_TEXT = "alt_text";
            public static final String DISPLAY_ORDER = "display_order";
            public static final String SITE_ID = "site_id";

            private Pictures() {}
        }

        public static final class Texts {
            public static final String TRANSLATION_KEY = "translation_key";
            public static final String CONTENT_VALUE = "content_value";
            public static final String SITE_ID = "site_id";
            public static final String LANGUAGE = "language";

            private Texts() {}
        }

        public static final class Banners {
            public static final String BANNER_KEY = "banner_key";
            public static final String POSITION = "position";
            public static final String VISIBLE = "visible";
            public static final String SITE_ID = "site_id";

            private Banners() {}
        }

        public static final class SiteImages {
            public static final String IMAGE_KEY = "image_key";
            public static final String STORAGE_URL = "storage_url";
            public static final String SITE_ID = "site_id";

            private SiteImages() {}
        }
    }
}
