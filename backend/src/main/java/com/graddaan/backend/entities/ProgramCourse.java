package com.graddaan.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "program_courses")
@IdClass(ProgramCourseId.class)
public class ProgramCourse {

    @Id
    @Column(name = "program_id")
    private Long programId;

    @Id
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "is_required")
    private Boolean isRequired;

    @ManyToOne
    @JoinColumn(name = "program_id", insertable = false, updatable = false)
    private Program program;

    @ManyToOne
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private Course course;
}
