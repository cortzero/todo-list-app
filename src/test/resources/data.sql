INSERT INTO users (id, first_name, last_name, username, email, password) VALUES
(1, 'Test', 'User', 'testuser', 'test@example.com', '123');

INSERT INTO to_dos (id, task, complete, user_id) VALUES
(1, 'Do something', false, 1),
(2, 'Do something else', false, 1),
(3, 'Do another thing', false, 1);