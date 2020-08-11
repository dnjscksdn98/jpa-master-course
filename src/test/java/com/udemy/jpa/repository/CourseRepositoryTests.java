package com.udemy.jpa.repository;

import com.udemy.jpa.JpaApplication;
import com.udemy.jpa.models.Course;
import com.udemy.jpa.models.Review;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JpaApplication.class)
class CourseRepositoryTests {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void findById() {
        Course course = courseRepository.findById(10001L);

        assertEquals("TEST01", course.getName());
    }

    @Test
    @DirtiesContext
    public void deleteById() {
        courseRepository.deleteById(10001L);
        Course course = courseRepository.findById(10001L);

        assertNull(course);
    }

    @Test
    @DirtiesContext
    public void save() {
        Course course = courseRepository.findById(10001L);
        assertEquals("TEST01", course.getName());

        course.setName(("UPDATE TEST01"));
        courseRepository.save(course);

        Course updated = courseRepository.findById(10001L);
        assertEquals("UPDATE TEST01", updated.getName());
    }

    @Test
    @DirtiesContext
    public void playWithEntityManager() {
        courseRepository.playWithEntityManager();
    }

    @Test
    @Transactional
    public void retrieveReviewsForCourse() {
        Course course = courseRepository.findById(10001L);
        logger.info(String.format("Course reviews [%s]", course.getReviews()));
    }

    @Test
    @Transactional
    public void retrieveCourseForReview() {
        Review review = entityManager.find(Review.class, 50001L);
        logger.info(String.format("Review course [%s]", review.getCourse()));
    }

}