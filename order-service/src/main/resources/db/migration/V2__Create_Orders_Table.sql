CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    cost INT CHECK (cost >= 0 AND cost <= 1000000000),
    status INT, -- EnumType.ORDINAL stores enum as integer
    created_at TIMESTAMP
);
