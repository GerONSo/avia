CREATE TABLE posts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    flight_id UUID NOT NULL,
    image_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_published BOOLEAN NOT NULL,
    title TEXT,
    CONSTRAINT fk_user_2 FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_flight FOREIGN KEY (flight_id) REFERENCES flights (id)
);

CREATE TABLE user_subscriptions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    subscribed_to_user_id UUID NOT NULL,
    CONSTRAINT fk_user_3 FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_4 FOREIGN KEY (subscribed_to_user_id) REFERENCES users (id),
    UNIQUE (user_id, subscribed_to_user_id)
);
