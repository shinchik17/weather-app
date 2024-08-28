INSERT INTO users (login, password)
VALUES ('test_username1', 'test_pass1'),
       ('test_username2', 'test_pass2');

INSERT INTO locations (name, user_id, latitude, longitude)
VALUES ('test_loc_name1', 1, 0.1, 0.2),
       ('test_loc_name2', 2, 0.1, 0.2),
       ('test_loc_name3', 1, 0.1, 0.2);


INSERT INTO sessions (id, user_id, expires_at)
VALUES ('test_session_id1', 1, CURRENT_TIMESTAMP),
       ('test_session_id2', 2, CURRENT_TIMESTAMP - INTERVAL '1 YEAR');
