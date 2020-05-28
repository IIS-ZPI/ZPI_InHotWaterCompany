DROP TABLE IF EXISTS state;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS product;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

CREATE TABLE state (
	id							INTEGER		PRIMARY KEY AUTOINCREMENT,
	name						TEXT		NOT NULL,
	base_tax					REAL		DEFAULT 0.0,
	groceries_tax				REAL		DEFAULT 0.0,
	prepared_food_tax			REAL		DEFAULT 0.0,
	prescription_drug_tax		REAL		DEFAULT 0.0,
	nonprescription_drug_tax	REAL		DEFAULT 0.0,
	clothing_tax				REAL		DEFAULT 0.0,
	intangibles_tax				REAL		DEFAULT 0.0
);

CREATE TABLE category (
	id			INTEGER		PRIMARY KEY AUTOINCREMENT,
	name		TEXT		NOT NULL
);

CREATE TABLE product (
	id			INTEGER		PRIMARY KEY AUTOINCREMENT,
	name		TEXT		NOT NULL,
	category	INTEGER		REFERENCES category(id),
	price		REAL		DEFAULT 0.0
);

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

INSERT INTO category(name) VALUES
	('Groceries'),
	('Prepared food'),
	('Prescription drug'),
	('Non-prescription drug'),
	('Clothing'),
	('Intangibles');

INSERT INTO state(name, base_tax, groceries_tax, prepared_food_tax, prescription_drug_tax, nonprescription_drug_tax, clothing_tax, intangibles_tax) VALUES
	('Alabama',					4.0,	-1.0,	-1.0,	0.0,	-1.0,	-1.0,	-1.0),
	('Alaska',					0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0),
	('Arizona',					5.6,	0.0,	-1.0,	0.0,	-1.0,	-1.0,	-1.0),
	('Arkansas',				6.5,	0.125,	-1.0,	0.0,	-1.0,	-1.0,	0.0),
	('California',				7.25,	0.0,	-1.0,	0.0,	-1.0,	-1.0,	0.0),
	('Colorado',				2.9,	0.0,	-1.0,	0.0,	-1.0,	-1.0,	-1.0),
	('Connecticut',				6.35,	0.0,	-1.0,	0.0,	0.0,	-1.0,	1.0),
	('Delaware',				0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0),
	('District of Columbia',	5.75,	0.0,	10.0,	0.0,	0.0,	-1.0,	-1.0),
	('Florida',					6.0,	0.0,	9.0,	0.0,	-1.0,	-1.0,	0.0),
	('Georgia',					4.0,	4.0,	-1.0,	0.0,	-1.0,	-1.0,	0.0),
	('Guam',					4.0,	-1.0,	-1.0,	-1.0,	-1.0,	-1.0,	-1.0),
	('Hawaii',					4.166,	-1.0,	-1.0,	-1.0,	-1.0,	-1.0,	-1.0),
	('Idaho',					6.0,	-1.0,	-1.0,	0.0,	-1.0,	-1.0,	-1.0),
	('Illinois',				6.25,	1.0,	8.25,	1.0,	1.0,	-1.0,	0.0),
	('Indiana',					7.0,	0.0,	9.0,	0.0,	-1.0,	-1.0,	-1.0),
	('Iowa',					6.0,	0.0,	-1.0,	0.0,	-1.0,	-1.0,	0.0),
	('Kansas',					6.5,	-1.0,	-1.0,	-1.0,	-1.0,	-1.0,	0.0),
	('Kentucky',				6.0,	0.0,	-1.0,	0.0,	-1.0,	-1.0,	-1.0),
	('Louisiana',				4.45,	7.0,	-1.0,	-1.0,	-1.0,	-1.0,	-1.0),
	('Maine',					5.5,	0.0,	-1.0,	0.0,	-1.0,	-1.0,	-1.0),
	('Maryland',				6.0,	0.0,	-1.0,	0.0,	-1.0,	-1.0,	-1.0),
	('Massachusetts',			6.25,	0.0,	7.0,	0.0,	-1.0,	0.0,	0.0),
	('Michigan',				6.0,	0.0,	-1.0,	0.0,	-1.0,	-1.0,	0.0),
	('Minnesota',				6.875,	0.0,	10.755,	0.0,	-1.0,	0.0,	-1.0),
	('Mississippi',				7.0,	-1.0,	-1.0,	-1.0,	-1.0,	-1.0,	-1.0),
	('Missouri',				4.225,	1.225,	-1.0,	-1.0,	-1.0,	-1.0,	0.0),
	('Montana',					0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0),
	('Nebraska',				5.5,	0.0,	9.5,	-1.0,	-1.0,	-1.0,	-1.0),
	('Nevada',					6.85,	0.0,	-1.0,	0.0,	-1.0,	-1.0,	0.0),
	('New Hampshire',			0.0,	0.0,	9.0,	0.0,	0.0,	0.0,	0.0),
	('New Jersey',				6.625,	0.0,	-1.0,	0.0,	0.0,	0.0,	-1.0),
	('New Mexico',				5.125,	0.0,	-1.0,	-1.0,	-1.0,	-1.0,	-1.0),
	('New York',				4.0,	0.0,	-1.0,	0.0,	0.0,	0.0,	0.0),
	('North Carolina',			4.75,	2.0,	8.5,	0.0,	-1.0,	-1.0,	-1.0),
	('North Dakota',			5.0,	0.0,	-1.0,	-1.0,	-1.0,	-1.0,	0.0),
	('Ohio',					5.75,	0.0,	-1.0,	0.0,	-1.0,	-1.0,	-1.0),
	('Oklahoma',				4.5,	-1.0,	-1.0,	-1.0,	-1.0,	-1.0,	0.0),
	('Oregon',					0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0),
	('Pennsylvania',			6.0,	0.0,	-1.0,	0.0,	0.0,	0.0,	-1.0),
	('Puerto Rico',				10.5,	1.0,	-1.0,	0.0,	-1.0,	-1.0,	-1.0),
	('Rhode Island',			7.0,	0.0,	8.0,	0.0,	-1.0,	0.0,	0.0),
	('South Carolina',			6.0,	0.0,	10.5,	0.0,	-1.0,	-1.0,	0.0),
	('South Dakota',			4.0,	-1.0,	-1.0,	-1.0,	-1.0,	-1.0,	-1.0),
	('Tennessee',				7.0,	4.0,	-1.0,	-1.0,	-1.0,	-1.0,	-1.0),
	('Texas',					6.25,	0.0,	-1.0,	0.0,	0.0,	-1.0,	-1.0),
	('Utah',					5.95,	3.0,	-1.0,	0.0,	-1.0,	-1.0,	-1.0),
	('Vermont',					6.0,	0.0,	9.0,	0.0,	0.0,	0.0,	-1.0),
	('Virginia',				5.3,	2.5,	5.3,	0.0,	0.0,	-1.0,	0.0),
	('Washington',				6.5,	0.0,	10.0,	0.0,	-1.0,	-1.0,	-1.0),
	('West Virginia',			6.0,	0.0,	-1.0,	0.0,	-1.0,	-1.0,	0.0),
	('Wisconsin',				5.0,	0.0,	-1.0,	0.0,	-1.0,	-1.0,	-1.0),
	('Wyoming',					4.0,	0.0,	-1.0,	0.0,	-1.0,	-1.0,	-1.0);
