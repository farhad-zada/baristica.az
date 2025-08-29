CREATE TABLE IF NOT EXISTS user_balance (
    user_id   INTEGER PRIMARY KEY,
    balance   INTEGER CHECK (balance >= 0)
);
