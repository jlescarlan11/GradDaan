package com.graddaan.backend.dtos;

import java.math.BigDecimal;

import com.graddaan.backend.entities.Semester;

import lombok.Data;

@Data
public class UserCourseUpdateDto {

    private Semester semesterTaken;
    private Integer yearTaken;
    private BigDecimal grade;
}
