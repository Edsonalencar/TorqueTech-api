CREATE TABLE IF NOT EXISTS vehicle (
    id UUID PRIMARY KEY NOT NULL,
    license_plate VARCHAR(20) NOT NULL UNIQUE,
    color VARCHAR(50),
    year VARCHAR(10) NOT NULL,
    vehicle_type_id UUID,
    customer_id UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_vehicle_type FOREIGN KEY (vehicle_type_id) REFERENCES vehicle_types(id) ON DELETE SET NULL,
    CONSTRAINT fk_vehicle_customer FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
);