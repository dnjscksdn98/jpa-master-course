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
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JpaApplication.class)
public class NativeQueriesTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager entityManager;

    @Test
    public void findAllWithNativeQuery() {
        Query query = entityManager.createNativeQuery("SELECT * FROM courses", Course.class);
        List<?> courses = query.getResultList();
        logger.info(String.format("SELECT * FROM courses -> %s", courses));
    }
}
