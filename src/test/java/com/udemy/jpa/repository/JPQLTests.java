package com.udemy.jpa.repository;

import com.udemy.jpa.JpaApplication;
import com.udemy.jpa.models.Course;
import com.udemy.jpa.models.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JpaApplication.class)
public class JPQLTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager entityManager;

    @Test
    public void findAllWithQuery() {
        Query query = entityManager.createQuery("SELECT C FROM Course C");
        List<?> courses = query.getResultList();
        logger.info(String.format("SELECT C FROM Course C -> %s", courses));
    }

    @Test
    public void findAllWithTypedQuery() {
        TypedQuery<Course> query = entityManager.createQuery("SELECT C FROM Course C", Course.class);
        List<Course> courses = query.getResultList();
        logger.info(String.format("SELECT C FROM Course C -> %s", courses));
    }

    @Test
    public void findAllWithWhere() {
        TypedQuery<Course> query = entityManager.createQuery("SELECT C FROM Course C WHERE name LIKE '%1'", Course.class);
        List<Course> courses = query.getResultList();
        logger.info(String.format("SELECT C FROM Course C WHERE name LIKE '%%1' -> %s", courses));
    }

    @Test
    public void findAllWithNamedQuery() {
        TypedQuery<Course> query = entityManager.createNamedQuery("query_get_all_courses", Course.class);
        List<Course> courses = query.getResultList();
        logger.info(String.format("SELECT C FROM Course C -> %s", courses));
    }

    /**
     * Queries with entity relationships using JPQL
     */

    @Test
    public void findCoursesWithoutStudents() {
        TypedQuery<Course> query = entityManager.createQuery("SELECT C FROM Course C WHERE C.students is empty", Course.class);
        List<Course> courses = query.getResultList();
        logger.info(String.format("Courses without students -> {%s}", courses));
    }

    @Test
    public void findCoursesAtLeastTwoStudents() {
        TypedQuery<Course> query = entityManager.createQuery("SELECT C FROM Course C WHERE SIZE(C.students) >= 2", Course.class);
        List<Course> courses = query.getResultList();
        logger.info(String.format("Courses with at least 2 students -> {%s}", courses));
    }

    @Test
    public void findCoursesAndOrderByAscStudents() {
        TypedQuery<Course> query = entityManager.createQuery("SELECT C FROM Course C ORDER BY SIZE(C.students)", Course.class);
        List<Course> courses = query.getResultList();
        logger.info(String.format("Courses ordered by students -> {%s}", courses));
    }

    @Test
    public void findCoursesAndOrderByDescStudents() {
        TypedQuery<Course> query = entityManager.createQuery("SELECT C FROM Course C ORDER BY SIZE(C.students) DESC", Course.class);
        List<Course> courses = query.getResultList();
        logger.info(String.format("Courses ordered by students -> {%s}", courses));
    }

    @Test
    public void findStudentsWithCertainPassportPattern() {
        TypedQuery<Student> query = entityManager.createQuery("SELECT S FROM Student S WHERE S.passport.number LIKE '%17389%'", Student.class);
        List<Student> students = query.getResultList();
        logger.info(String.format("Students with certain passport pattern -> {%s}", students));
    }

    /**
     * (INNER) JOIN -> SELECT C, S FROM Course C JOIN C.students S
     * LEFT (OUTER) JOIN -> SELECT C, S FROM Course C LEFT JOIN C.students S
     * CROSS JOIN -> SELECT C, S FROM Course C, Student S
     */

    @Test
    public void join() {
        // 배열의 형태로 반환합니다
        Query query = entityManager.createQuery("SELECT C, S FROM Course C JOIN C.students S");
        List<Object[]> resultList = query.getResultList();
        logger.info(String.format("Results size -> {%s}", resultList.size()));
        resultList.stream()
                .forEach(result -> logger.info(String.format("Course{%s} Student{%s}", result[0], result[1])));
    }

    @Test
    public void leftJoin() {
        // 배열의 형태로 반환합니다
        Query query = entityManager.createQuery("SELECT C, S FROM Course C LEFT JOIN C.students S");
        List<Object[]> resultList = query.getResultList();
        logger.info(String.format("Results size -> {%s}", resultList.size()));
        resultList.stream()
                .forEach(result -> logger.info(String.format("Course{%s} Student{%s}", result[0], result[1])));
    }

    @Test
    public void crossJoin() {
        // 배열의 형태로 반환합니다
        Query query = entityManager.createQuery("SELECT C, S FROM Course C, Student S");
        List<Object[]> resultList = query.getResultList();
        logger.info(String.format("Results size -> {%s}", resultList.size()));
        resultList.stream()
                .forEach(result -> logger.info(String.format("Course{%s} Student{%s}", result[0], result[1])));
    }
}
