package com.graddaan.backend.dtos;

import java.math.BigDecimal;

import com.graddaan.backend.entities.Semester;

import lombok.Data;

@Data
public class UserCourseDto {

    private Long userId;
    private Long courseId;
    private BigDecimal grade;
    private Semester semesterTaken;
    private Integer yearTaken;
}
