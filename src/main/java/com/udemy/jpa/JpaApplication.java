package com.udemy.jpa;

import com.udemy.jpa.models.FullTimeEmployee;
import com.udemy.jpa.models.PartTimeEmployee;
import com.udemy.jpa.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class JpaApplication implements CommandLineRunner {

	@Autowired
	private EmployeeRepository employeeRepository;

	private final static Logger logger = LoggerFactory.getLogger(JpaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(JpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		employeeRepository.insert(
				FullTimeEmployee
						.builder()
						.name("Jack")
						.salary(new BigDecimal("10000"))
						.build());

		employeeRepository.insert(
				PartTimeEmployee
						.builder()
						.name("Jill")
						.hourlyWage(new BigDecimal("50"))
						.build());

		logger.info(String.format("Full Time Employees -> {%s}", employeeRepository.retrieveAllFullTimeEmployees()));
		logger.info(String.format("Part Time Employees -> {%s}", employeeRepository.retrieveAllPartTimeEmployees()));
	}
}
