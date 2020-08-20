package com.udemy.jpa.repository;

import com.udemy.jpa.models.Employee;
import com.udemy.jpa.models.FullTimeEmployee;
import com.udemy.jpa.models.PartTimeEmployee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class EmployeeRepository {

    private final static Logger logger = LoggerFactory.getLogger(EmployeeRepository.class);

    @Autowired
    private EntityManager entityManager;

    public void insert(Employee employee) {
        entityManager.persist(employee);
    }

    public List<PartTimeEmployee> retrieveAllPartTimeEmployees() {
        return entityManager.createQuery("SELECT E FROM PartTimeEmployee E", PartTimeEmployee.class).getResultList();
    }

    public List<FullTimeEmployee> retrieveAllFullTimeEmployees() {
        return entityManager.createQuery("SELECT E FROM FullTimeEmployee E", FullTimeEmployee.class).getResultList();
    }
}
