package com.udemy.jpa;

import com.udemy.jpa.models.Review;
import com.udemy.jpa.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class JpaApplication implements CommandLineRunner {

	@Autowired
	private CourseRepository courseRepository;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(JpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Long courseId = 10003L;
		List<Review> reviews = new ArrayList<>();
		reviews.add(Review.builder().rating("5").description("Awesome!").build());
		reviews.add(Review.builder().rating("1").description("Bad...").build());
		courseRepository.addReviewsForCourse(courseId, reviews);
	}
}
