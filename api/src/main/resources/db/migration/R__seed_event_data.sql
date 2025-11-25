-------------------------------------------------------------
-- SEED: Events
-------------------------------------------------------------

INSERT INTO events (
    id, organizer_id, name, description, start_date, end_date, venue, status
) VALUES
      -- 1) Draft event (editable)
      ('11111111-1111-1111-1111-111111111111',
       'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
       'Tech Conference 2025',
       'A conference about emerging technologies.',
       NOW() + INTERVAL '10 days',
       NOW() + INTERVAL '11 days',
       'Casablanca Convention Center',
       'DRAFT'),

      -- 2) Published event (in ticket sales phase)
      ('22222222-2222-2222-2222-222222222222',
       'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
       'Music Festival Summer Edition',
       'Outdoor music event with multiple artists.',
       NOW() + INTERVAL '20 days',
       NOW() + INTERVAL '21 days',
       'Rabat Open Arena',
       'PUBLISHED'),

      -- 3) Closed event (already finished)
      ('33333333-3333-3333-3333-333333333333',
       'cccccccc-cccc-cccc-cccc-cccccccccccc',
       'Startup Pitch Night',
       'A competition for early-stage startups.',
       NOW() - INTERVAL '20 days',
       NOW() - INTERVAL '19 days',
       'Marrakech Innovation Hub',
       'CLOSED');

-------------------------------------------------------------
-- SEED: Ticket Types for each event
-------------------------------------------------------------

-- Ticket types for Event 1 (Draft)
INSERT INTO ticket_types (
    id, name, price, max_quantity, sold_quantity, event_id
) VALUES
      ('aaaaaaaa-1111-1111-1111-aaaaaaaaaaaa',
       'Standard Pass', 100.00, 200, 0,
       '11111111-1111-1111-1111-111111111111'),

      ('bbbbbbbb-1111-1111-1111-bbbbbbbbbbbb',
       'VIP Pass', 250.00, 50, 0,
       '11111111-1111-1111-1111-111111111111');

-- Ticket types for Event 2 (Published)
INSERT INTO ticket_types (
    id, name, price, max_quantity, sold_quantity, event_id
) VALUES
      ('aaaaaaaa-2222-2222-2222-aaaaaaaaaaaa',
       'General Admission', 150.00, 1000, 120,
       '22222222-2222-2222-2222-222222222222'),

      ('bbbbbbbb-2222-2222-2222-bbbbbbbbbbbb',
       'VIP Zone', 350.00, 100, 40,
       '22222222-2222-2222-2222-222222222222');

-- Ticket types for Event 3 (Closed)
INSERT INTO ticket_types (
    id, name, price, max_quantity, sold_quantity, event_id
) VALUES
    ('aaaaaaaa-3333-3333-3333-aaaaaaaaaaaa',
     'Pitch Night Entry', 80.00, 200, 200,
     '33333333-3333-3333-3333-333333333333');
