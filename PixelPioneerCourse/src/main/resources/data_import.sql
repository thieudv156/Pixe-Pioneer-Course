INSERT INTO roles (role_name)
VALUES ('ROLE_ADMIN'), ('ROLE_USER'), ('ROLE_INSTRUCTOR');

INSERT INTO users (role_id, username, password, email, full_name, phone, active_status, created_at)
VALUES (1, 'anhtuan040507', 'anhtuan123', 'anhtuan200745@gmail.com', 'NVAT', '0838150169', true, '2022-01-01');
VALUES (1, 'thieu', 'anhtuan123', 'thieu@gmail.com', 'NVT', '0838150168', true, '2022-01-01');
VALUES (1, 'khanh', 'anhtuan123', 'khanh@gmail.com', 'KHANH', '0838150167', true, '2022-01-01');

INSERT INTO categories (name)
VALUES ('Computer Science'),('Data Science'),('Business'),('Mathematics'),('Physics'),('Chemistry'),('Biology'),('Engineering'),('Arts'),('Music');

INSERT INTO courses (title, category_id, price, instructor_id, is_published, created_at, updated_at,image_id, description)
VALUES ('Advanced Computer Science', 1, 200.0, 1, true, '2022-02-01', '2022-02-01', 1, 'This is a course about Advanced Computer Science')
     ,('Advanced Computer Science', 1, 200.0, 1, true, '2022-02-01', '2022-02-01', 2, 'This is a course about Advanced Computer Science'),
       ('Advanced Computer Science', 1, 200.0, 1, true, '2022-02-01', '2022-02-01',3, 'This is a course about Advanced Computer Science');