CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    firebase_id VARCHAR(255) NOT NULL,
    image_url VARCHAR(255)
);

CREATE TABLE achievements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    is_hidden BOOLEAN,
    text TEXT
);

INSERT INTO achievements (name, image_url, is_hidden, text)
VALUES 
    ('distance', 'https://storage.yandexcloud.net/aviascan-public/achievements/distance_h.png', FALSE, 'Wow! Such a long distance!'),
    ('distance', 'https://storage.yandexcloud.net/aviascan-public/achievements/distance_n.png', TRUE, NULL),
    ('time', 'https://storage.yandexcloud.net/aviascan-public/achievements/time_h.png', FALSE, 'You have been in the air for so long!'),
    ('time', 'https://storage.yandexcloud.net/aviascan-public/achievements/time_n.pn', TRUE, NULL);

CREATE TABLE user_achievements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    achievement_id UUID NOT NULL,
    CONSTRAINT fk_achievement FOREIGN KEY (achievement_id) REFERENCES achievements (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE flights (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    airport_from VARCHAR(10) NOT NULL,
    airport_to VARCHAR(10) NOT NULL,
    date VARCHAR(16),
    flight_code VARCHAR(16),
    flight_distance REAL,
    flight_time REAL,
    user_id UUID NOT NULL,
    CONSTRAINT fk_user_1 FOREIGN KEY (user_id) REFERENCES users (id)
);