INSERT INTO roles (role_name)
VALUES ('ROLE_ADMIN'), ('ROLE_USER'), ('ROLE_INSTRUCTOR');

INSERT INTO users (role_id, username, password, email, full_name, phone, active_status, created_at)
VALUES (1, 'anhtuan040507', 'anhtuan123', 'anhtuan200745@gmail.com', 'NVAT', '0838150169', true, '2022-01-01');

INSERT INTO categories (name)
VALUES ('Computer Science'),('Data Science'),('Business'),('Mathematics'),('Physics'),('Chemistry'),('Biology'),('Engineering'),('Arts'),('Music');

INSERT INTO pixel_pioneer_course.courses (category_id, instructor_id, is_published, price, created_at, updated_at, description, image, title) VALUES (1, 1, true, 100, '2024-05-29 23:15:23.000000', '2024-05-29 23:15:27.000000', 'Khoa hoc Laravel', '1.jpg', 'Test khoa hoc'),
(2, 1, true, 100, '2024-05-29 23:15:23.000000', '2024-05-29 23:15:27.000000', 'Khoa hoc Python', '2.jpg', 'Test khoa hoc'),
(3, 1, true, 100, '2024-05-29 23:15:23.000000', '2024-05-29 23:15:27.000000', 'Khoa hoc Business', '3.jpg', 'Test khoa hoc'),
(4, 1, true, 100, '2024-05-29 23:15:23.000000', '2024-05-29 23:15:27.000000', 'Khoa hoc Mathematics', '4.jpg', 'Test khoa hoc'),
(5, 1, true, 100, '2024-05-29 23:15:23.000000', '2024-05-29 23:15:27.000000', 'Khoa hoc Physics', '5.jpg', 'Test khoa hoc'),
(6, 1, true, 100, '2024-05-29 23:15:23.000000', '2024-05-29 23:15:27.000000', 'Khoa hoc Chemistry', '6.jpg', 'Test khoa hoc'),
(7, 1, true, 100, '2024-05-29 23:15:23.000000', '2024-05-29 23:15:27.000000', 'Khoa hoc Biology', '7.jpg', 'Test khoa hoc'),
(8, 1, true, 100, '2024-05-29 23:15:23.000000', '2024-05-29 23:15:27.000000', 'Khoa hoc Engineering', '8.jpg', 'Test khoa hoc'),
(9, 1, true, 100, '2024-05-29 23:15:23.000000', '2024-05-29 23:15:27.000000', 'Khoa hoc Arts', '9.jpg', 'Test khoa hoc'),
(10, 1, true, 100, '2024-05-29 23:15:23.000000', '2024-05-29 23:15:27.000000', 'Khoa hoc Music', '10.jpg', 'Test khoa hoc');


INSERT INTO pixel_pioneer_course.lessons (complete_status, course_id, created_at, image, title) VALUES (true, 1, '2024-05-29 23:18:42.000000', '1.jpg', 'Tt Lesson'),
(true, 1, '2024-05-29 23:18:42.000000', '2.jpg', 'Tt Lesson'),
(true, 1, '2024-05-29 23:18:42.000000', '3.jpg', 'Tt Lesson'),
(true, 1, '2024-05-29 23:18:42.000000', '4.jpg', 'Tt Lesson');

INSERT INTO pixel_pioneer_course.sub_lessons (complete_status, lesson_id, created_at, content, image, title) VALUES (true, 1, '2024-05-29 23:24:16.000000', 'Sublesson 1', '2.jpg', 'Sublesson 111111'),
(true, 1, '2024-05-29 23:24:16.000000', 'Sublesson 2', '3.jpg', 'Sublesson 222222'),
(true, 1, '2024-05-29 23:24:16.000000', 'Sublesson 3', '4.jpg', 'Sublesson 333333'),
(true, 1, '2024-05-29 23:24:16.000000', 'Sublesson 4', '5.jpg', 'Sublesson 444444');


# INSERT INTO pixel_pioneer_course.comments (course_id, parent_id, user_id, created_at, content) VALUES (1, 1, 2, '2024-05-29 23:25:53.000000', 'Test Discussion'),
# (1, 1, 2, '2024-05-29 23:25:53.000000', 'Test Discussion 1'),
# (1, 1, 2, '2024-05-29 23:25:53.000000', 'Test Discussion 2'),
# (1, 1, 2, '2024-05-29 23:25:53.000000', 'Test Discussion 3');

INSERT INTO pixel_pioneer_course.discussions (parent_id, sub_lesson_id, user_id, created_at, content) VALUES (1, 1, 2, '2024-05-29 23:29:10.000000', 'Discussion 1'),
(1, 1, 2, '2024-05-29 23:29:10.000000', 'Discussion 2'),
(1, 1, 2, '2024-05-29 23:29:10.000000', 'Discussion 3'),
(1, 1, 2, '2024-05-29 23:29:10.000000', 'Discussion 4');