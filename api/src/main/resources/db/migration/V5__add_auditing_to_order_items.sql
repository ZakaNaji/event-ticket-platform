alter table order_items
add column created_at timestamp not null,
add column last_modified timestamp;