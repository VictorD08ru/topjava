DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
-- ALTER SEQUENCE global_seq RESTART WITH 100000; --do not work
SELECT setval('global_seq', 100000, false);

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id) VALUES
  ('2018-07-29T10:00'::timestamp, 'Завтрак', 500, 100000 ),
  ('2018-07-29T13:00'::timestamp, 'Обед', 1000, 100000 ),
  ('2018-07-29T20:00'::timestamp, 'Ужин', 500, 100000 ),
  ('2018-07-30T10:00'::timestamp, 'Завтрак', 500, 100000 ),
  ('2018-07-30T13:00'::timestamp, 'Обед', 1000, 100000 ),
  ('2018-07-30T20:00'::timestamp, 'Ужин', 500, 100000 ),
  ('2018-07-31T10:00'::timestamp, 'Завтрак', 500, 100000 ),
  ('2018-07-31T13:00'::timestamp, 'Обед', 1000, 100000 ),
  ('2018-07-31T20:00'::timestamp, 'Ужин', 510, 100000 ),
  ('2018-08-01T07:00'::timestamp, 'Завтрак админа', 200, 100001 ),
  ('2018-08-01T14:00'::timestamp, 'Ланч админа', 1510, 100001 ),
  ('2018-08-01T21:00'::timestamp, 'Ужин админа', 510, 100001 );