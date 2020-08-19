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
@Table(name = "part_time_employees")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartTimeEmployee extends Employee {

    private BigDecimal hourlyWage;

    @Builder
    public PartTimeEmployee(String name, BigDecimal hourlyWage) {
        super(name);
        this.hourlyWage = hourlyWage;
    }
}
