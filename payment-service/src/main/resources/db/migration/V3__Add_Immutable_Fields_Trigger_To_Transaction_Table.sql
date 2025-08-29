CREATE OR REPLACE FUNCTION transaction_prevent_update_trigger() 
RETURNS TRIGGER AS $$
BEGIN 
    IF NEW.id != OLD.id THEN 
        RAISE EXCEPTION 'Feld "id" cannot be updated!';
    END IF;
    IF NEW.user_id != OLD.user_id THEN 
        RAISE EXCEPTION 'Feld "user_id" cannot be updated!';
    END IF;
    IF NEW.amount != OLD.amount THEN 
        RAISE EXCEPTION 'Feld "amount" cannot be updated!';
    END IF;
    IF NEW.created_at != OLD.created_at THEN 
        RAISE EXCEPTION 'Feld "created_at" cannot be updated!';
    END IF;
END;
$$ LANGUAGE PLPGSQL;

CREATE TRIGGER trg_prevent_update_immutable_fields 
BEFORE UPDATE ON transactions
FOR EACH ROW 
EXECUTE FUNCTION transaction_prevent_update_trigger();