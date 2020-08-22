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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JpaApplication.class)
public class PerformanceTuningTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceTuningTests.class);

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    public void NPlusOneProblemTest() {
        List<Course> courses = entityManager
                .createNamedQuery("query_get_all_courses", Course.class)
                .getResultList();

        courses
                .forEach(course -> LOGGER.info(String.format("Course -> {%s} Students -> {%s}", course, course.getStudents())));
    }

    @Test
    @Transactional
    public void solvingNPlusOneProblemWithEntityGraph() {
        EntityGraph<Course> entityGraph = entityManager.createEntityGraph(Course.class);
        entityGraph.addSubgraph("students");

        List<Course> courses = entityManager
                .createNamedQuery("query_get_all_courses", Course.class)
                .setHint("javax.persistence.loadgraph", entityGraph)
                .getResultList();

        courses
                .forEach(course -> LOGGER.info(String.format("Course -> {%s} Students -> {%s}", course, course.getStudents())));
    }

    @Test
    @Transactional
    public void solvingNPlusOneProblemWithJoinFetch() {
        List<Course> courses = entityManager
                .createNamedQuery("query_get_all_courses_join_fetch", Course.class)
                .getResultList();

        courses
                .forEach(course -> LOGGER.info(String.format("Course -> {%s} Students -> {%s}", course, course.getStudents())));
    }
}
