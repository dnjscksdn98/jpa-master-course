package com.udemy.jpa.models;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "full_time_employees")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FullTimeEmployee extends Employee {

    private BigDecimal salary;

    @Builder
    public FullTimeEmployee(String name, BigDecimal salary) {
        super(name);
        this.salary = salary;
    }
}
