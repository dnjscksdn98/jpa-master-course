package com.udemy.jpa;

import com.udemy.jpa.models.Course;
import com.udemy.jpa.repository.CourseSpringDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@SpringBootApplication
public class JpaApplication implements CommandLineRunner {

	@Autowired
	private CourseSpringDataRepository courseRepository;

	private final static Logger LOGGER = LoggerFactory.getLogger(JpaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(JpaApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
//		courseRepository.findById(10001L);
//		logger.info("Find course 10001");
//
//		courseRepository.deleteById(10001L);
//		logger.info("Delete course 10001");
	}
}
