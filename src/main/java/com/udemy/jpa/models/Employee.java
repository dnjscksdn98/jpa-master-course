package com.udemy.jpa.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
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
