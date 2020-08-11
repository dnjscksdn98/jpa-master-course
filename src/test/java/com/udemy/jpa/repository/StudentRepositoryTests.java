package com.udemy.jpa.repository;

import com.udemy.jpa.JpaApplication;
import com.udemy.jpa.models.Passport;
import com.udemy.jpa.models.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JpaApplication.class)
class StudentRepositoryTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    public void retrieveStudentAndPassportDetails() {
        Student student = entityManager.find(Student.class, 20001L);
        logger.info(String.format("Student[%s]", student.getName()));
        logger.info(String.format("Passport[%s]", student.getPassport().getNumber()));
    }

    @Test
    @Transactional
    public void retrievePassportAndAssociatedStudent() {
        Passport passport = entityManager.find(Passport.class, 30001L);
        logger.info(String.format("Passport[%s]", passport.getNumber()));
        logger.info(String.format("Student[%s]", passport.getStudent().getName()));
    }

    @Test
    @Transactional  // Persistence Context
    public void transactionalTest() {
        // Create Persistence Context

        // Database Operation 1 - Retrieve student
        Student student = entityManager.find(Student.class, 20001L);
        // Persistence Context(student)

        // Database Operation 2 - Retrieve passport
        Passport passport = student.getPassport();
        // Persistence Context(student, passport)

        // Database Operation 3 - Update passport
        passport.setNumber("U98989");
        // Persistence Context(student, passport++)

        // Database Operation 4 - Update student
        student.setName("Alex - updated");
        // Persistence Context(student++, passport++)

        // Persist to Database

        // Kill Persistence Context
    }

}