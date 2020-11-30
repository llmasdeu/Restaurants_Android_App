CREATE TABLE users(
    _id INT PRIMARY KEY,
    name VARCHAR(255) DEFAULT '',
    surname VARCHAR(255) DEFAULT '',
    username VARCHAR(255) DEFAULT '',
    email VARCHAR(255) DEFAULT '',
    password VARCHAR(255) DEFAULT '',
    gender VARCHAR(6) DEFAULT 'other',
    description VARCHAR(255) DEFAULT '',
    image VARCHAR(100) DEFAULT ''
);

CREATE TABLE searches(
    _id INT PRIMARY KEY,
    search VARCHAR(255) DEFAULT ''
);

CREATE TABLE favorites(
    _id INT PRIMARY KEY,
    username VARCHAR(255),
    name VARCHAR(255),
    type VARCHAR(255),
    address VARCHAR(300),
    opening VARCHAR(50),
    closing VARCHAR(50),
    rating DOUBLE,
    description VARCHAR(1000),
    latitude DOUBLE,
    longitude DOUBLE,
    image VARCHAR(100)
);

CREATE TABLE commentaries(
    _id INT PRIMARY KEY,
    username VARCHAR(255),
    name VARCHAR(255),
    type VARCHAR(255),
    address VARCHAR(300),
    commentary VARCHAR(1000)
);

