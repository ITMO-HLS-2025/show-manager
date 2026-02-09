insert into performance (id, title, description, duration_minutes)
values (3, 'Don Quixote', 'Ballet', 150);

insert into "show" (id, performance_id, hall_id, show_time)
values (12, 3, 101, CURRENT_TIMESTAMP + INTERVAL '2 day');
