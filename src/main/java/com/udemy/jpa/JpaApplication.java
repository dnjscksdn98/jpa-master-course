package com.udemy.jpa;

import com.udemy.jpa.models.Course;
import com.udemy.jpa.repository.CourseSpringDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;


@SpringBootApplication
public class JpaApplication implements CommandLineRunner {

	@Autowired
	private CourseSpringDataRepository courseRepository;

	private final static Logger logger = LoggerFactory.getLogger(JpaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(JpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
        Optional<Course> course = courseRepository.findById(10001L);
        Optional<Course> course2 = courseRepository.findById(10001L);
	}
}
