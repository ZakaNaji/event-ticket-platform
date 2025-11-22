 create table tickets (
     id uuid primary key,
     order_id uuid not null,
     event_id uuid not null ,
     ticket_type_id uuid not null ,
     ticket_type_name varchar(200) not null ,
     price numeric(10,2) not null ,
     status varchar(20) not null ,
     qr_code varchar(250) not null unique
 );

create index idx_ticket_order_id on tickets(order_id);
create index idx_ticket_event_id on tickets(event_id);
create index idx_ticket_ticket_type_id on tickets(ticket_type_id);
create index idx_tickets_qr_code on tickets(qr_code);

---

create table check_in_log (
    id uuid primary key ,
    ticket_id uuid not null ,
    staff_id uuid not null ,
    scan_time timestamp not null ,
    result varchar(20) not null ,
    constraint fk_check_in_log_tickets
        foreign key (ticket_id)
        references tickets(id)
        on delete cascade
);

create index idx_check_in_log_ticket_id on check_in_log(ticket_id);
create index idx_check_in_log_staff_id on check_in_log(staff_id);
create index idx_check_in_log_scan_time on check_in_log(scan_time);
