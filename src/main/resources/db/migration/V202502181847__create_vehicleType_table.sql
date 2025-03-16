CREATE TABLE IF NOT EXISTS vehicle_types (
    id UUID PRIMARY KEY NOT NULL,
    model VARCHAR(255) NOT NULL,
    brand VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    owner_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_vehicle_types_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);