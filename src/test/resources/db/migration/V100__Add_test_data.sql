INSERT INTO users (login, password)
VALUES ('test_username1', 'test_pass1'),
       ('test_username2', 'test_pass2'),
       ('test_username3', 'test_pass3'),
       ('delete_username', 'test_pass4');

INSERT INTO locations (name, user_id, latitude, longitude)
VALUES ('test_loc1', 2, 0.1, 0.2),
       ('test_loc2', 2, 0.1, 0.2),
       ('test_loc3', 3, 0.1, 0.2);

INSERT INTO sessions (id, user_id, expires_at)
VALUES ('test_string_id1', 3, current_timestamp),
       ('test_string_id2', 3, current_timestamp),
       ('test_string_id3', 3, current_timestamp);
