package com.udemy.jpa.repository;

import com.udemy.jpa.models.Course;
import com.udemy.jpa.models.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

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

    public void addReviewsForCourse() {
        // get the course
        Course course = findById(10003L);

        // add 2 reviews to course + setting the relationship
        Review review1 = Review.builder().rating("5").description("Awesome!").build();
        course.addReview(review1);
        review1.setCourse(course);

        Review review2 = Review.builder().rating("1").description("Bad...").build();
        course.addReview(review2);
        review2.setCourse(course);

        // save to database
        entityManager.persist(review1);
        entityManager.persist(review2);
    }

    public void addReviewsForCourse(Long courseId, List<Review> reviews) {
        // get the course
        Course course = findById(courseId);

        reviews
                .stream()
                .forEach(review -> {
                    course.addReview(review);
                    review.setCourse(course);
                    entityManager.persist(review);
                });
    }
}
