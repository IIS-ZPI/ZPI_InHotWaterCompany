DROP TABLE IF EXISTS state;
DROP TABLE IF EXISTS state_tax;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS logistic_cost;
DROP TABLE IF EXISTS import_country;
DROP TABLE IF EXISTS import_cost;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

CREATE TABLE state (
	id      INTEGER    PRIMARY KEY AUTOINCREMENT,
	name    TEXT       NOT NULL,
	code    TEXT       NOT NULL
);

CREATE TABLE state_tax (
	id                          INTEGER    PRIMARY KEY AUTOINCREMENT,
	state                       INTEGER    REFERENCES state(id),
	base_tax                    REAL       DEFAULT 0.0,
	groceries_tax               REAL       DEFAULT 0.0,
	prepared_food_tax           REAL       DEFAULT 0.0,
	prescription_drug_tax       REAL       DEFAULT 0.0,
	nonprescription_drug_tax    REAL       DEFAULT 0.0,
	clothing_tax                REAL       DEFAULT 0.0,
	intangibles_tax             REAL       DEFAULT 0.0
);

CREATE TABLE category (
	id      INTEGER    PRIMARY KEY AUTOINCREMENT,
	name    TEXT       NOT NULL
);

CREATE TABLE product (
	id          INTEGER    PRIMARY KEY AUTOINCREMENT,
	name        TEXT       NOT NULL,
	category    INTEGER    REFERENCES category(id),
	price       REAL       DEFAULT 0.0
);

CREATE TABLE logistic_cost (
	id               INTEGER    PRIMARY KEY AUTOINCREMENT,
	state            INTEGER    REFERENCES state(id),
	transport_fee    REAL       DEFAULT 0.0
);

CREATE TABLE import_country (
	id          INTEGER    PRIMARY KEY AUTOINCREMENT,
	name        TEXT       NOT NULL,
	code        TEXT       NOT NULL,
	currency    TEXT       NOT NULL
);

CREATE TABLE import_cost (
	id                          INTEGER    PRIMARY KEY AUTOINCREMENT,
	import_country              INTEGER    REFERENCES import_country(id),
	transport_fee               REAL       DEFAULT 0.0,
	consumables_import_tariff   REAL       DEFAULT 0.0,
	others_import_tariff        REAL       DEFAULT 0.0
);

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

INSERT INTO category(name) VALUES
	('Groceries'),
	('Prepared food'),
	('Prescription drug'),
	('Non-prescription drug'),
	('Clothing'),
	('Intangibles');

INSERT INTO state(name, code) VALUES
	('Alabama', 'AL'),
	('Alaska', 'AK'),
	('Arizona', 'AZ');

INSERT INTO state_tax(state, base_tax, groceries_tax, prepared_food_tax, prescription_drug_tax, nonprescription_drug_tax, clothing_tax, intangibles_tax) VALUES
	(1,     4.0,      -1.0,     -1.0,      0.0,     -1.0,    -1.0,    -1.0),
	(2,     0.0,      0.0,      0.0,       0.0,     0.0,     0.0,     0.0),
	(3,     5.6,      0.0,      -1.0,      0.0,     -1.0,    -1.0,    -1.0);

INSERT INTO logistic_cost(state) SELECT id FROM state;
