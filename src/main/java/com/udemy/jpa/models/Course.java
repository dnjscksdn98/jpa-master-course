package com.udemy.jpa.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@NamedQuery(name = "query_get_all_courses", query = "SELECT C FROM Course C")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @UpdateTimestamp
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Override
    public String toString() {
        return String.format("Course[%s]", this.name);
    }
}
