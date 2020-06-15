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
	('Arizona', 'AZ'),
	('Arkansas', 'AR'),
	('California', 'CA'),
	('Colorado', 'CO'),
	('Connecticut', 'CT'),
	('Delaware', 'DE'),
	('Florida', 'FL'),
	('Georgia', 'GA'),
	('Hawaii', 'HI'),
	('Idaho', 'ID'),
	('Illinois', 'IL'),
	('Indiana', 'IN'),
	('Iowa', 'IA'),
	('Kansas', 'KS'),
	('Kentucky', 'KY'),
	('Louisiana', 'LA'),
	('Maine', 'ME'),
	('Maryland', 'MD'),
	('Massachusetts', 'MA'),
	('Michigan', 'MI'),
	('Minnesota', 'MN'),
	('Mississippi', 'MS'),
	('Missouri', 'MO'),
	('Montana', 'MT'),
	('Nebraska', 'NE'),
	('Nevada', 'NV'),
	('New Hampshire', 'NH'),
	('New Jersey', 'NJ'),
	('New Mexico', 'NM'),
	('New York', 'NY'),
	('North Carolina', 'NC'),
	('North Dakota', 'ND'),
	('Ohio', 'OH'),
	('Oklahoma', 'OK'),
	('Oregon', 'OR'),
	('Pennsylvania', 'PA'),
	('Rhode Island', 'RI'),
	('South Carolina', 'SC'),
	('South Dakota', 'SD'),
	('Tennessee', 'TN'),
	('Texas', 'TX'),
	('Utah', 'UT'),
	('Vermont',	'VT'),
	('Virginia', 'VA'),
	('Washington', 'WA'),
	('West Virginia', 'WV'),
	('Wisconsin', 'WI'),
	('Wyoming', 'WY');

INSERT INTO state_tax(state, base_tax, groceries_tax, prepared_food_tax, prescription_drug_tax, nonprescription_drug_tax, clothing_tax, intangibles_tax) VALUES
	(1,     4.0,      -1.0,     -1.0,      0.0,     -1.0,    -1.0,    -1.0),
	(2,     0.0,      0.0,      0.0,       0.0,     0.0,     0.0,     0.0),
	(3,     5.6,      0.0,      -1.0,      0.0,     -1.0,    -1.0,    -1.0),
	(4,     6.5,      0.125,    -1.0,      0.0,     -1.0,    -1.0,    0.0),
	(5,     7.25,     0.0,      -1.0,      0.0,     -1.0,    -1.0,    0.0),
	(6,     2.9,      0.0,      -1.0,      0.0,     -1.0,    -1.0,    -1.0),
	(7,     6.35,     0.0,      -1.0,      0.0,     0.0,     -1.0,    1.0),
	(8,     0.0,      0.0,      0.0,       0.0,     0.0,     0.0,     0.0),
	(9,     6.0,      0.0,      9.0,       0.0,     -1.0,    -1.0,    0.0),
	(10,    4.0,      4.0,      -1.0,      0.0,     -1.0,    -1.0,    0.0),
	(11,    4.166,    -1.0,     -1.0,      -1.0,    -1.0,    -1.0,    -1.0),
	(12,    6.0,      -1.0,     -1.0,      0.0,     -1.0,    -1.0,    -1.0),
	(13,    6.25,     1.0,      8.25,      1.0,     1.0,     -1.0,    0.0),
	(14,    7.0,      0.0,      9.0,       0.0,     -1.0,    -1.0,    -1.0),
	(15,    6.0,      0.0,      -1.0,      0.0,     -1.0,    -1.0,    0.0),
	(16,    6.5,      -1.0,     -1.0,      -1.0,    -1.0,    -1.0,    0.0),
	(17,    6.0,      0.0,      -1.0,      0.0,     -1.0,    -1.0,    -1.0),
	(18,    4.45,     7.0,      -1.0,      -1.0,    -1.0,    -1.0,    -1.0),
	(19,    5.5,      0.0,      -1.0,      0.0,     -1.0,    -1.0,    -1.0),
	(20,    6.0,      0.0,      -1.0,      0.0,     -1.0,    -1.0,    -1.0),
	(21,    6.25,      0.0,     7.0,       0.0,     -1.0,     0.0,    0.0),
	(22,    6.0,      0.0,      -1.0,      0.0,     -1.0,    -1.0,    0.0),
	(23,    6.875,     0.0,     10.755,    0.0,     -1.0,    0.0,     -1.0),
	(24,    7.0,      -1.0,     -1.0,      -1.0,    -1.0,    -1.0,    -1.0),
	(25,    4.225,    1.225,    -1.0,      -1.0,    -1.0,    -1.0,    0.0),
	(26,    0.0,      0.0,      0.0,       0.0,     0.0,     0.0,     0.0),
	(27,    5.5,      0.0,      9.5,       -1.0,    -1.0,    -1.0,    -1.0),
	(28,    6.85,     0.0,      -1.0,      0.0,     -1.0,    -1.0,    0.0),
	(29,    0.0,      0.0,      9.0,       0.0,     0.0,     0.0,     0.0),
	(30,    6.625,    0.0,      -1.0,      0.0,     0.0,     0.0,     -1.0),
	(31,    5.125,    0.0,      -1.0,      -1.0,    -1.0,    -1.0,    -1.0),
	(32,    4.0,      0.0,      -1.0,      0.0,     0.0,     0.0,     0.0),
	(33,    4.75,     2.0,      8.5,       0.0,     -1.0,    -1.0,    -1.0),
	(34,    5.0,      0.0,      -1.0,      -1.0,    -1.0,    -1.0,    0.0),
	(35,    5.75,     0.0,      -1.0,      0.0,     -1.0,    -1.0,    -1.0),
	(36,    4.5,      -1.0,     -1.0,      -1.0,    -1.0,    -1.0,    0.0),
	(37,    0.0,      0.0,      0.0,       0.0,     0.0,     0.0,     0.0),
	(38,    6.0,      0.0,      -1.0,      0.0,     0.0,     0.0,     -1.0),
	(39,    7.0,      0.0,      8.0,       0.0,     -1.0,    0.0,     0.0),
	(40,    6.0,      0.0,      10.5,      0.0,     -1.0,    -1.0,    0.0),
	(41,    4.0,      -1.0,     -1.0,      -1.0,    -1.0,    -1.0,    -1.0),
	(42,    7.0,      4.0,      -1.0,      -1.0,    -1.0,    -1.0,    -1.0),
	(43,    6.25,     0.0,      -1.0,      0.0,     0.0,     -1.0,    -1.0),
	(44,    5.95,     3.0,      -1.0,      0.0,     -1.0,    -1.0,    -1.0),
	(45,    6.0,      0.0,      9.0,       0.0,     0.0,     0.0,     -1.0),
	(46,    5.3,      2.5,      5.3,       0.0,     0.0,     -1.0,    0.0),
	(47,    6.5,      0.0,      10.0,      0.0,     -1.0,    -1.0,    -1.0),
	(48,    6.0,      0.0,      -1.0,      0.0,     -1.0,    -1.0,    0.0),
	(49,    5.0,      0.0,      -1.0,      0.0,     -1.0,    -1.0,    -1.0),
	(50,    4.0,      0.0,      -1.0,      0.0,     -1.0,    -1.0,    -1.0);

INSERT INTO logistic_cost(state) SELECT id FROM state;