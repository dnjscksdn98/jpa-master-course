package com.udemy.jpa.repository;

import com.udemy.jpa.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSpringDataRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByName(String name);

    Optional<Course> findByNameAndId(String name, Long id);

    Long countByName(String name);

    List<Course> findAllOrderByIdDesc();

    void deleteByName(String name);

    // JPQL
    @Query(value = "SELECT C FROM Course C WHERE name LIKE '%Spring%'")
    List<Course> findAllWithSpring();

    // Native Query
    @Query(value = "SELECT C FROM courses C WHERE name LIKE '%Spring%'", nativeQuery = true)
    List<Course> findAllWithSpringUsingNativeQuery();

    // Named Query
    @Query(name = "query_get_all_courses")
    List<Course> findAllWithSpringUsingNamedQuery();
}
