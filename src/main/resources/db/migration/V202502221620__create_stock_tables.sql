-- V2__create_stock_tables.sql

-- Tabela Item
CREATE TABLE IF NOT EXISTS item (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    description TEXT,
    vehicle_type_id UUID,
    garage_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_item_vehicle_type FOREIGN KEY (vehicle_type_id) REFERENCES vehicle_types(id) ON DELETE SET NULL,
    CONSTRAINT fk_item_garage FOREIGN KEY (garage_id) REFERENCES garage(id)
);

-- Tabela Local
CREATE TABLE IF NOT EXISTS local (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    garage_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_local_garage FOREIGN KEY (garage_id) REFERENCES garage(id)
);

-- Tabela StockItem
CREATE TABLE IF NOT EXISTS stock_item (
    id UUID PRIMARY KEY NOT NULL,
    acquisition_price BIGINT,
    price BIGINT,
    quantity INTEGER,
    item_id UUID NOT NULL,
    local_id UUID NULL,
    garage_id UUID NOT NULL,
    acquisition_at DATE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_stock_item_item FOREIGN KEY (item_id) REFERENCES item(id),
    CONSTRAINT fk_stock_item_local FOREIGN KEY (local_id) REFERENCES local(id),
    CONSTRAINT fk_stock_item_garage FOREIGN KEY (garage_id) REFERENCES garage(id)
);

-- Tabela StockTransaction
CREATE TABLE IF NOT EXISTS stock_transaction (
    id UUID PRIMARY KEY NOT NULL,
    price BIGINT,
    quantity INTEGER,
    category VARCHAR(50),
    status VARCHAR(50),
    type VARCHAR(50),
    garage_id UUID NOT NULL,
    owner_id UUID NOT NULL,
    work_order_id UUID NULL,
    transaction_date TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_stock_transaction_garage FOREIGN KEY (garage_id) REFERENCES garage(id),
    CONSTRAINT fk_stock_transaction_owner FOREIGN KEY (owner_id) REFERENCES users(id),
    CONSTRAINT fk_stock_transaction_work_order FOREIGN KEY (work_order_id) REFERENCES work_order(id)
);

-- Criar a tabela TransactionItem
CREATE TABLE IF NOT EXISTS transaction_item (
    id UUID PRIMARY KEY NOT NULL,
    quantity INTEGER NOT NULL,
    stock_item_id UUID NOT NULL,
    transaction_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_transaction_item_stock_item FOREIGN KEY (stock_item_id) REFERENCES stock_item(id),
    CONSTRAINT fk_transaction_item_transaction FOREIGN KEY (transaction_id) REFERENCES stock_transaction(id)
);
