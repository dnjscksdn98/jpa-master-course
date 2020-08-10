package com.udemy.jpa.repository;

import com.udemy.jpa.models.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class CourseRepository {

    @Autowired
    private EntityManager entityManager;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Course findById(Long id) {
        return entityManager.find(Course.class, id);
    }

    public void deleteById(Long id) {
        Course course = findById(id);
        entityManager.remove(course);
    }

    // insert & update
    public Course save(Course course) {
        if(course.getId() == null) {
            // insert
            entityManager.persist(course);
        }
        else {
            //update
            entityManager.merge(course);
        }
        return course;
    }

    public void playWithEntityManager() {
        logger.info("Play with Entity Manager Start................");

        Course course = Course.builder().name("NEW COURSE").build();
        entityManager.persist(course);  // save
        Course course2 = Course.builder().name("NEW COURSE 2").build();
        entityManager.persist(course2);  // save
        entityManager.flush();

        course.setName("UPDATE COURSE");  // update
        course2.setName("UPDATE COURSE 2");  // update
        entityManager.flush();
    }
}
