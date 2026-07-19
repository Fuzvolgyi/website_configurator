CREATE TABLE apartment_prices (
    id BIGSERIAL PRIMARY KEY,
    apartment_slug VARCHAR(50) NOT NULL UNIQUE,
    apartment_name VARCHAR(100) NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL
);

INSERT INTO apartment_prices (apartment_slug, apartment_name, price_per_night) VALUES
    ('studio-views', 'Studio Apartment with Views', 90.00),
    ('family', 'Family Apartment', 120.00),
    ('studio', 'Studio Apartment', 85.00),
    ('family-room', 'Family Room', 105.00),
    ('double-comfort', 'Double Comfort Room', 95.00);
