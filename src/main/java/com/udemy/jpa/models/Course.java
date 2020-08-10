package com.udemy.jpa.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "courses")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public String toString() {
        return String.format("Course[%s]", this.name);
    }
}
