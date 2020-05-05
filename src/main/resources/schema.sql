-- Incorrect, need fix :(

CREATE TABLE IF NOT EXISTS state (
	id			INTEGER		PRIMARY KEY AUTOINCREMENT,
	tax			REAL		DEFAULT 0.0
);

CREATE TABLE IF NOT EXISTS category (
	id			INTEGER		PRIMARY KEY AUTOINCREMENT,
	name		TEXT		NOT NULL
);

CREATE TABLE IF NOT EXISTS product (
	id			INTEGER		PRIMARY KEY AUTOINCREMENT,
	name		TEXT		NOT NULL,
	category	INTEGER		REFERENCES category(id),
	price		REAL		DEFAULT 0.0
);

CREATE VIEW IF NOT EXISTS product_list AS
SELECT
	product.id,
	product.name,
	category.name AS category,
	product.price
FROM product
JOIN category ON product.category = category.id;
