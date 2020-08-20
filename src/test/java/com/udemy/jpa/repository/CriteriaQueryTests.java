package com.udemy.jpa.repository;

import com.udemy.jpa.JpaApplication;
import com.udemy.jpa.models.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JpaApplication.class)
public class CriteriaQueryTests {

    private final static Logger logger = LoggerFactory.getLogger(CriteriaQueryTests.class);

    @Autowired
    private EntityManager entityManager;

    /**
     * Criteria Query -> To create a query using Java
     */
    @Test
    public void findAllCourses() {
        // SELECT C FROM Course C

        // 1. Use Criteria Builder to create a Criteria Query returning the expected result object
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        // 2. Define roots for tables which are involved in the query
        Root<Course> root = criteriaQuery.from(Course.class);

        // 3. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = entityManager.createQuery(criteriaQuery.select(root));

        List<Course> courses = query.getResultList();
        logger.info(String.format("Courses -> {%s}", courses));
    }

    /**
     * Add WHERE to Criteria Query
     */
    @Test
    public void findCoursesWithCertainPattern() {
        // SELECT C FROM Course C WHERE name LIKE '%JPA%'

        // 1. Use Criteria Builder to create a Criteria Query returning the expected result object
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        // 2. Define roots for tables which are involved in the query
        Root<Course> root = criteriaQuery.from(Course.class);

        // 3. Define Predicates etc using Criteria Builder
        Predicate like = criteriaBuilder.like(root.get("name"), "%JPA%");

        // 4. Add Predicates etc to the Criteria Query
        criteriaQuery.where(like);

        // 5. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = entityManager.createQuery(criteriaQuery.select(root));

        List<Course> courses = query.getResultList();
        logger.info(String.format("Courses -> {%s}", courses));
    }

    @Test
    public void findCoursesWithoutStudents() {
        // JPQL -> SELECT C FROM Course C WHERE C.students IS EMPTY

        // 1. Use Criteria Builder to create a Criteria Query returning the expected result object
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        // 2. Define roots for tables which are involved in the query
        Root<Course> root = criteriaQuery.from(Course.class);

        // 3. Define Predicates etc using Criteria Builder
        Predicate isEmpty = criteriaBuilder.isEmpty(root.get("students"));

        // 4. Add Predicates etc to the Criteria Query
        criteriaQuery.where(isEmpty);

        // 5. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = entityManager.createQuery(criteriaQuery.select(root));

        List<Course> courses = query.getResultList();
        logger.info(String.format("Courses -> {%s}", courses));
    }

    /**
     * (INNER) JOIN
     * LEFT (OUTER) JOIN
     */

    @Test
    public void join() {
        // SELECT C FROM Course C JOIN C.students S

        // 1. Use Criteria Builder to create a Criteria Query returning the expected result object
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        // 2. Define roots for tables which are involved in the query
        Root<Course> root = criteriaQuery.from(Course.class);

        // 3. Define Join using Root
        Join<Object, Object> join = root.join("students");

        // 4. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = entityManager.createQuery(criteriaQuery.select(root));

        List<Course> courses = query.getResultList();
        logger.info(String.format("Courses -> {%s}", courses));
    }

    @Test
    public void leftJoin() {
        // SELECT C FROM Course C LEFT JOIN C.students S

        // 1. Use Criteria Builder to create a Criteria Query returning the expected result object
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);

        // 2. Define roots for tables which are involved in the query
        Root<Course> root = criteriaQuery.from(Course.class);

        // 3. Define Join using Root
        Join<Object, Object> join = root.join("students", JoinType.LEFT);

        // 4. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = entityManager.createQuery(criteriaQuery.select(root));

        List<Course> courses = query.getResultList();
        logger.info(String.format("Courses -> {%s}", courses));
    }
}
