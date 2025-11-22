CREATE TABLE events (
    id uuid primary key,
    organizer_id uuid not null,
    name varchar(200) not null,
    description text,
    start_date timestamp not null,
    end_date timestamp not null,
    venue varchar(250) not null,
    status varchar(20) not null
);

create table ticket_types (
    id uuid primary key,
    name varchar(200) not null,
    price numeric(10,2) not null,
    max_quantity integer not null,
    sold_quantity integer not null,
    event_id uuid not null,
    constraint fk_ticket_type_event
        foreign key (event_id)
        references events(id)
        on delete cascade
);

create index idx_events_start_date on events(start_date);
create index idx_ticket_type_event_id on ticket_types(event_id);