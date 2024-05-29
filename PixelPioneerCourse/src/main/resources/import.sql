-- Insert initial roles
INSERT INTO roles (role_name) VALUES
                                  ('ROLE_STUDENT'),
                                  ('ROLE_ADMIN'),
                                  ('ROLE_INSTRUCTOR');

-- Insert initial categories
INSERT INTO categories (name) VALUES
                                  ('Computer Science'),
                                  ('Data Analysis'),
                                  ('Physics');

-- Insert initial users
-- Note: Ensure role names are unique to avoid issues with SELECT statements
INSERT INTO users (role_id, username, password, email, full_name, phone, active_status, created_at) VALUES
                                                                                                        ((SELECT id FROM roles WHERE role_name = 'ROLE_ADMIN' LIMIT 1), 'admin', 'admin123', 'admin@example.com', 'Admin User', '1234567890', TRUE, CURDATE()),
                                                                                                        ((SELECT id FROM roles WHERE role_name = 'ROLE_INSTRUCTOR' LIMIT 1), 'instructor', 'instructor123', 'instructor@example.com', 'Instructor User', '0987654321', TRUE, CURDATE()),
                                                                                                        ((SELECT id FROM roles WHERE role_name = 'ROLE_STUDENT' LIMIT 1), 'anhtuan040507', 'anhtuan123', 'student@example.com', 'NVAT', '0838150169', TRUE, CURDATE());
