insert into performance (id, title, description, duration_minutes)
values (1, 'Hamlet', 'Classic drama', 120);

insert into performance (id, title, description, duration_minutes)
values (2, 'Swan Lake', 'Ballet', 140);

insert into "show" (id, performance_id, hall_id, show_time)
values (10, 1, 100, CURRENT_TIMESTAMP + INTERVAL '1 day');

insert into "show" (id, performance_id, hall_id, show_time)
values (11, 2, 101, CURRENT_TIMESTAMP + INTERVAL '1 day');
