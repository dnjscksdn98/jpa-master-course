INSERT INTO courses(id, name, created_date, updated_date) VALUES(10001, 'Spring MSA', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10002, 'Spring Data JPA', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10003, 'Spring Boot', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10004, 'Spring Cloud', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10005, 'Microservices', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10006, 'Java Design Patterns', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10007, 'Docker', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10008, 'Kubernetes', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10009, 'Spring Security', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10010, 'Spring Batch', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10011, 'Javascript', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10012, 'Typescript', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10013, 'C++', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10014, 'Blockchain', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10015, 'Etherium', NOW(), NOW());

INSERT INTO passports(id, number, created_date, updated_date) VALUES(30001, 'E17389', NOW(), NOW());
INSERT INTO passports(id, number, created_date, updated_date) VALUES(30002, 'Z19479', NOW(), NOW());
INSERT INTO passports(id, number, created_date, updated_date) VALUES(30003, 'A99174', NOW(), NOW());

INSERT INTO students(id, name, passport_id, created_date, updated_date) VALUES(20001, 'Alex', 30001, NOW(), NOW());
INSERT INTO students(id, name, passport_id, created_date, updated_date) VALUES(20002, 'Park', 30002, NOW(), NOW());
INSERT INTO students(id, name, passport_id, created_date, updated_date) VALUES(20003, 'Kim', 30003, NOW(), NOW());

INSERT INTO reviews(id, rating, description, course_id, created_date, updated_date) VALUES(50001, 'FOUR', 'Very good', 10001, NOW(), NOW());
INSERT INTO reviews(id, rating, description, course_id, created_date, updated_date) VALUES(50002, 'THREE', 'Pretty good', 10002, NOW(), NOW());
INSERT INTO reviews(id, rating, description, course_id,created_date, updated_date) VALUES(50003, 'TWO', 'Terrible', 10003, NOW(), NOW());

INSERT INTO student_course(student_id, course_id) VALUES(20001, 10001);
INSERT INTO student_course(student_id, course_id) VALUES(20002, 10001);
INSERT INTO student_course(student_id, course_id) VALUES(20003, 10001);
INSERT INTO student_course(student_id, course_id) VALUES(20001, 10003);