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
@Table(name = "prerequisites")
@IdClass(PrerequisiteId.class)
public class Prerequisite {

    @Id
    @Column(name = "course_id")
    private Long courseId;

    @Id
    @Column(name = "prerequisite_course_id")
    private Long prerequisiteCourseId;

    @ManyToOne
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "prerequisite_course_id", insertable = false, updatable = false)
    private Course prerequisiteCourse;
}
