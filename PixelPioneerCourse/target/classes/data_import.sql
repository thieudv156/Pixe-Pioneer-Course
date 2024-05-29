INSERT INTO roles (role_name)
VALUES ('ROLE_ADMIN'), ('ROLE_USER'), ('ROLE_INSTRUCTOR');

INSERT INTO users (role_id, username, password, email, full_name, phone, active_status, created_at)
VALUES (1, 'anhtuan040507', 'anhtuan123', 'anhtuan200745@gmail.com', 'NVAT', '0838150169', true, '2022-01-01');

INSERT INTO categories (name)
VALUES ('Computer Science'),('Data Science'),('Business'),('Mathematics'),('Physics'),('Chemistry'),('Biology'),('Engineering'),('Arts'),('Music');