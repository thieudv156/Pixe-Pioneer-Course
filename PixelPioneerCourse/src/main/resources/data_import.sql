INSERT INTO roles (role_name)
VALUES ('ROLE_ADMIN'), ('ROLE_USER'), ('ROLE_INSTRUCTOR');


INSERT INTO categories (name)
VALUES ('Computer Science'),('Data Science'),('Business'),('Mathematics'),('Physics'),('Chemistry'),('Biology'),('Engineering'),('Arts'),('Music');

insert into images(image_name, image_url, image_type)
values('default.jpg','D:\\Pixe-Pioneer-Course\\PixelPioneerCourse\\src\\main\\resources\\static\\public\\images\\default.jpg','image/jpg');

INSERT INTO questions (correct_answer, question, wrong_answer1, wrong_answer2, wrong_answer3, course_id) VALUES
                                                                                                   ('C. Constructor', 'Which of the following is used to initialize an object in Java?', 'A. Method', 'B. Variable', 'D. Class', 1),
                                                                                                   ('B. new', 'Which keyword is used to create an object in Java?', 'A. create', 'C. instance', 'D. object', 1),
                                                                                                   ('A. import', 'Which keyword is used to include the functionality of another class in Java?', 'B. package', 'C. include', 'D. extend', 1),
                                                                                                   ('B. throw', 'Which keyword is used to manually throw an exception in Java?', 'A. catch', 'C. try', 'D. finally', 1),
                                                                                                   ('D. Bytecode', 'What is the intermediate representation of Java code called?', 'A. Machine code', 'B. Assembly code', 'C. Source code', 1),
                                                                                                   ('A. javac', 'Which command is used to compile a Java program?', 'B. java', 'C. javadoc', 'D. javap',1),
                                                                                                   ('C. javap', 'Which command is used to disassemble a Java class file?', 'A. javac', 'B. java', 'D. javadoc', 1),
                                                                                                   ('B. Overriding', 'What is it called when a subclass provides a specific implementation of a method that is already defined in its superclass?', 'A. Overloading', 'C. Encapsulation', 'D. Abstraction', 1),
                                                                                                   ('D. Method overloading', 'Which of the following allows multiple methods in the same class with the same name but different parameters?', 'A. Method overriding', 'B. Method hiding', 'C. Method binding', 1),
                                                                                                   ('A. byte', 'Which of the following is the smallest integer data type in Java?', 'B. short', 'C. int', 'D. long', 1);
