CREATE TABLE performance (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration_minutes INT
);

CREATE TABLE "show" (
    id BIGSERIAL PRIMARY KEY,
    performance_id BIGINT NOT NULL,
    hall_id BIGINT NOT NULL,
    show_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_show_performance
        FOREIGN KEY (performance_id) REFERENCES performance(id)
);
