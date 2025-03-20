CREATE TABLE IF NOT EXISTS work_order (
    id UUID PRIMARY KEY NOT NULL,
    work_id UUID NOT NULL,

    start_at TIMESTAMP NOT NULL,
    concluded_at TIMESTAMP,
    expected_at TIMESTAMP,
    deleted_at TIMESTAMP,

    status VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    note TEXT,
    cost BIGINT,
    price BIGINT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_work FOREIGN KEY (work_id) REFERENCES work(id)
);