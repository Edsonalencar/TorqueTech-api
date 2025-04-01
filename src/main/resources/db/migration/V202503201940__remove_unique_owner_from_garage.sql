DO $$
DECLARE
    constraint_name text;
BEGIN
    SELECT tc.constraint_name INTO constraint_name
    FROM information_schema.table_constraints tc
    JOIN information_schema.key_column_usage kcu
      ON tc.constraint_name = kcu.constraint_name
    WHERE tc.table_name = 'garage'
      AND tc.constraint_type = 'UNIQUE'
      AND kcu.column_name = 'owner_id';

    IF constraint_name IS NOT NULL THEN
        EXECUTE format('ALTER TABLE garage DROP CONSTRAINT %I', constraint_name);
    END IF;
END $$;
