package com.udemy.jpa.repository;

import com.udemy.jpa.models.Course;
import com.udemy.jpa.models.Passport;
import com.udemy.jpa.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class StudentRepository {

    @Autowired
    private EntityManager entityManager;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Student findById(Long id) {
        return entityManager.find(Student.class, id);
    }

    public void deleteById(Long id) {
        Student student = findById(id);
        entityManager.remove(student);
    }

    // insert & update
    public Student save(Student student) {
        if(student.getId() == null) {
            // insert
            entityManager.persist(student);
        }
        else {
            //update
            entityManager.merge(student);
        }
        return student;
    }

    public void saveStudentWithPassport() {
        Passport passport = Passport.builder().number("Z12345").build();
        entityManager.persist(passport);

        Student student = Student.builder().name("Alex").passport(passport).build();
        entityManager.persist(student);
    }

    public void insertStudentAndCourse() {
        Student student = Student.builder().name("Lee").build();
        Course course = Course.builder().name("Java Functional Programming").build();
        entityManager.persist(student);
        entityManager.persist(course);

        student.addCourse(course);
        course.addStudent(student);

        // Persist the relationship owning side
        entityManager.persist(student);
    }
}
