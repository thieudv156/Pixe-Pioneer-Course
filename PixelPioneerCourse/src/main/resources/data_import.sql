INSERT INTO roles (role_name)
VALUES ('ROLE_ADMIN'), ('ROLE_USER'), ('ROLE_INSTRUCTOR');

INSERT INTO users (role_id, username, password, email, full_name, phone, active_status, created_at)
VALUES (1, 'anhtuan040507', 'anhtuan123', 'anhtuan200745@gmail.com', 'NVAT', '0838150169', true, '2022-01-01');

INSERT INTO categories (name)
VALUES ('Computer Science'),('Data Science'),('Business'),('Mathematics'),('Physics'),('Chemistry'),('Biology'),('Engineering'),('Arts'),('Music');

INSERT INTO pixel_pioneer_course.courses (created_at, description, image, is_published, price, published_date, title, updated_at, category_id, instructor_id) VALUES ('2024-05-30 15:06:59.000000', 'Des', 'f.jpg', true, 1000.3, '2024-05-30 15:05:40.000000', 'Title Laravel', '2024-05-30 15:05:50.000000', 2, 3),
('2024-05-30 15:06:59.000000', 'Des', 'f.jpg', true, 1000.3, '2024-05-30 15:05:40.000000', 'Title Python', '2024-05-30 15:05:50.000000', 2, 3),
('2024-05-30 15:06:59.000000', 'Des', 'f.jpg', true, 1000.3, '2024-05-30 15:05:40.000000', 'Title Business', '2024-05-30 15:05:50.000000', 2, 3),
('2024-05-30 15:06:59.000000', 'Des', 'f.jpg', true, 1000.3, '2024-05-30 15:05:40.000000', 'Title Mathematics', '2024-05-30 15:05:50.000000', 2, 3),
('2024-05-30 15:06:59.000000', 'Des', 'f.jpg', true, 1000.3, '2024-05-30 15:05:40.000000', 'Title Physics', '2024-05-30 15:05:50.000000', 2, 3),
('2024-05-30 15:06:59.000000', 'Des', 'f.jpg', true, 1000.3, '2024-05-30 15:05:40.000000', 'Title Chemistry', '2024-05-30 15:05:50.000000', 2, 3),
('2024-05-30 15:06:59.000000', 'Des', 'f.jpg', true, 1000.3, '2024-05-30 15:05:40.000000', 'Title Biology', '2024-05-30 15:05:50.000000', 2, 3);

INSERT INTO pixel_pioneer_course.lessons (complete_status, created_at, image, title, course_id) VALUES (true, '2024-05-30 15:11:38.000000', 'a.jpg', 'Title Lession', 2),
(true, '2024-05-30 15:11:38.000000', 'a.jpg', 'Title Lession', 2);

INSERT INTO pixel_pioneer_course.sub_lessons (complete_status, content, created_at, image, title, lesson_id) VALUES (true, 'Content Sublession', '2024-05-30 15:13:22.000000', 'a.jpg', 'Title sublesson', 3),
(true, 'Content Sublession', '2024-05-30 15:13:22.000000', 'a.jpg', 'Title sublesson', 3);


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


INSERT INTO pixel_pioneer_course.discussions (parent_id, sub_lesson_id, user_id, created_at, content) VALUES (1, 1, 2, '2024-05-29 23:29:10.000000', 'Discussion 1'),
(1, 1, 2, '2024-05-29 23:29:10.000000', 'Discussion 2'),
(1, 1, 2, '2024-05-29 23:29:10.000000', 'Discussion 3'),
(1, 1, 2, '2024-05-29 23:29:10.000000', 'Discussion 4');