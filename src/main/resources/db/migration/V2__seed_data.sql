INSERT INTO performance (id, title, description, duration_minutes)
VALUES (1, 'Щелкунчик', 'Балет Чайковского', 120),
       (2, 'Лебединое озеро', 'Классический балет', 130);

INSERT INTO theatre_performance (theatre_id, performance_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO "show" (id, performance_id, hall_id, show_time)
VALUES (1, 1, 1, now() + interval '1 day'),
       (2, 2, 3, now() + interval '2 days');
