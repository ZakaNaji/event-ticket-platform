create table orders (
    id uuid primary key,
    attendee_id uuid not null,
    event_id uuid not null,
    status varchar(20) not null,
    total_amount numeric(10,2) not null
);

create index idx_orders_event_id on orders(event_id);
create index idx_orders_status on orders(status);
create index idx_order_attendee_id on orders(attendee_id);

----

create table order_items (
    id uuid primary key,
    ticket_type_id uuid not null,
    ticket_type_name varchar(200) not null,
    quantity integer not null,
    unit_price numeric(10,2) not null,
    sub_total numeric(10,2) not null,
    order_id uuid not null,
    constraint fk_order_item_order
        foreign key (order_id)
        references orders(id)
        on delete cascade
);

create index idx_order_items_order_id on order_items(order_id);
create index idx_order_items_ticket_type_id on order_items(ticket_type_id);