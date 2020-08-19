package com.udemy.jpa.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "employees")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "EmployeeType")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Employee {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Employee[%s]", this.name);
    }
}
