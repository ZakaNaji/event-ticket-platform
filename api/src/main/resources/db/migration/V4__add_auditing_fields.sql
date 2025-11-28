ALTER TABLE orders
ADD COLUMN created_at timestamp not null,
ADD COLUMN last_modified timestamp