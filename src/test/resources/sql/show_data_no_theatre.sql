insert into performance (id, title, description, duration_minutes)
values (3, 'No Theatre', 'Missing theatre link', 90);

insert into "show" (id, performance_id, hall_id, show_time)
values (12, 3, 102, DATEADD('DAY', 1, CURRENT_TIMESTAMP));
