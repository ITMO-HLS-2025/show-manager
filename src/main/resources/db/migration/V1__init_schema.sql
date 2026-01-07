CREATE TABLE performance (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration_minutes INT
);

CREATE TABLE theatre_performance (
    performance_id BIGINT NOT NULL,
    theatre_id BIGINT NOT NULL,
    PRIMARY KEY (performance_id, theatre_id),
    CONSTRAINT fk_theatre_performance_performance
        FOREIGN KEY (performance_id) REFERENCES performance(id)
);

CREATE TABLE "show" (
    id BIGSERIAL PRIMARY KEY,
    performance_id BIGINT NOT NULL,
    hall_id BIGINT NOT NULL,
    show_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_show_performance
        FOREIGN KEY (performance_id) REFERENCES performance(id)
);
