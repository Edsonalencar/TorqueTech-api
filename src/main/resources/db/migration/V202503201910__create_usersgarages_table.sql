CREATE TABLE IF NOT EXISTS users_garage (
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID NOT NULL,
    garage_id UUID NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    is_primary_edit TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_users_garage_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_users_garage_garage FOREIGN KEY (garage_id) REFERENCES garage(id) ON DELETE CASCADE,
    
    CONSTRAINT unique_user_garage UNIQUE (user_id, garage_id)
);


-- Otimizando buscas
CREATE INDEX IF NOT EXISTS idx_users_garage_user ON users_garage(user_id);

CREATE INDEX IF NOT EXISTS idx_users_garage_garage ON users_garage(garage_id);

CREATE UNIQUE INDEX IF NOT EXISTS unique_primary_garage ON users_garage(user_id) WHERE is_primary = TRUE;
