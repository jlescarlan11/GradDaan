package com.graddaan.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_courses", indexes = {
    @Index(name = "idx_user_courses", columnList = "user_id, course_id"),
    @Index(name = "idx_semester_year", columnList = "user_id, year_taken, semester_taken"),
    @Index(name = "idx_course_status", columnList = "course_id, status")
})
public class UserCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "grade")
    private BigDecimal grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "semester_taken")
    private Semester semesterTaken = Semester.FIRST;

    @Column(name = "year_taken")
    private Integer yearTaken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.PASSED;

    @Column(name = "attempt_number")
    private Integer attemptNumber;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

}
