package com.udemy.jpa.repository;

import com.udemy.jpa.JpaApplication;
import com.udemy.jpa.models.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JpaApplication.class)
public class CourseSpringDataRepositoryTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseSpringDataRepositoryTests.class);

    @Autowired
    private CourseSpringDataRepository courseRepository;

    @Test
    public void findByIdWithExistingData() {
        Optional<Course> course = courseRepository.findById(10001L);
        LOGGER.info(String.format("Is course 10001 present? -> {%s}", course.isPresent()));

        assertTrue(course.isPresent());
    }

    @Test
    public void findByIdWithNonExistingData() {
        Optional<Course> course = courseRepository.findById(99999L);
        LOGGER.info(String.format("Is course 99999 present? -> {%s}", course.isPresent()));

        assertFalse(course.isPresent());
    }

    @Test
    @DirtiesContext
    public void saveAndUpdate() {
        Course newCourse = Course.builder().name("Java Design Patterns").build();
        courseRepository.save(newCourse);  // save

        newCourse.setName("Java Design Patterns - Updated");
        courseRepository.save(newCourse);  // update
    }

    @Test
    public void findAllCourses() {
        List<Course> courses = courseRepository.findAll();
        Long count = courseRepository.count();

        LOGGER.info(String.format("Number of courses -> %s", count));
        LOGGER.info(String.format("Courses -> {%s}", courses));
    }

    @Test
    public void findAllCoursesAndSortByName() {
        Sort sort = Sort.by(Sort.Direction.DESC, "name");
        List<Course> courses = courseRepository.findAll(sort);
        Long count = courseRepository.count();

        LOGGER.info(String.format("Number of courses -> %s", count));
        LOGGER.info(String.format("Courses -> {%s}", courses));
    }

    @Test
    public void findAllCoursesWithPagination() {
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<Course> firstPage = courseRepository.findAll(pageRequest);

        LOGGER.info(String.format("First page -> {%s}", firstPage.getContent()));

        Pageable secondPageable = firstPage.nextPageable();
        Page<Course> secondPage = courseRepository.findAll(secondPageable);

        LOGGER.info(String.format("Second page -> {%s}", secondPage.getContent()));
    }

    @Test
    public void findAllCoursesByName() {
        List<Course> courses = courseRepository.findAllByName("Spring Data JPA");

        LOGGER.info(String.format("Course -> {%s}", courses));
    }

    @Test
    @Transactional
    public void modifyingTest() {
        Course course = Course.of("before");
        courseRepository.save(course);

        int result = courseRepository.updateById(1L, "after");
        assertEquals(result, 1);

        LOGGER.info(String.format("Updated Course Name -> {%s}", courseRepository.findById(1L).get().getName()));
    }
}
