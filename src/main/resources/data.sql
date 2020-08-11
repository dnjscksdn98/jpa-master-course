INSERT INTO courses(id, name, created_date, updated_date) VALUES(10001, 'TEST01', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10002, 'TEST02', NOW(), NOW());
INSERT INTO courses(id, name, created_date, updated_date) VALUES(10003, 'TEST03', NOW(), NOW());

INSERT INTO passports(id, number, created_date, updated_date) VALUES(30001, 'PASSPORT01', NOW(), NOW());
INSERT INTO passports(id, number, created_date, updated_date) VALUES(30002, 'PASSPORT02', NOW(), NOW());
INSERT INTO passports(id, number, created_date, updated_date) VALUES(30003, 'PASSPORT03', NOW(), NOW());

INSERT INTO students(id, name, passport_id, created_date, updated_date) VALUES(20001, 'STUDENT01', 30001, NOW(), NOW());
INSERT INTO students(id, name, passport_id, created_date, updated_date) VALUES(20002, 'STUDENT02', 30002, NOW(), NOW());
INSERT INTO students(id, name, passport_id, created_date, updated_date) VALUES(20003, 'STUDENT03', 30003, NOW(), NOW());

INSERT INTO reviews(id, rating, description, created_date, updated_date) VALUES(50001, '4.5', 'Very good', NOW(), NOW());
INSERT INTO reviews(id, rating, description, created_date, updated_date) VALUES(50002, '3.5', 'Pretty good', NOW(), NOW());
INSERT INTO reviews(id, rating, description, created_date, updated_date) VALUES(50003, '2.0', 'Terrible', NOW(), NOW());