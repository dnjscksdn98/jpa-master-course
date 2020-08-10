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
}
