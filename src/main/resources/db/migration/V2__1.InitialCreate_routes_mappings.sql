CREATE TABLE routes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    gym_id BIGINT NOT NULL,
    name VARCHAR(100),            -- Optional (some routes just have a color)
    setter_name VARCHAR(100),
    image_url VARCHAR(500),       -- URL to S3
    official_beta_url VARCHAR(500), -- URL to S3/YouTube
    is_active BOOLEAN DEFAULT TRUE, -- TRUE = On the wall, FALSE = Stripped
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

-- Foreign Key to Gym
    CONSTRAINT fk_routes_gym FOREIGN KEY (gym_id) REFERENCES gyms(id) ON DELETE CASCADE,

-- Index for faster filtering of 'Active' routes in a specific Gym
    INDEX idx_routes_gym_active (gym_id, is_active)
);


CREATE TABLE route_tags (
    route_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,

-- Composite Primary Key ensures a tag is not added twice to the same route
    PRIMARY KEY (route_id, tag_id),

    CONSTRAINT fk_rt_route FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE,
    CONSTRAINT fk_rt_tag FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);
