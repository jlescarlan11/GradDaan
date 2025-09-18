package com.graddaan.backend.dtos;

import java.math.BigDecimal;

import com.graddaan.backend.entities.Semester;
import com.graddaan.backend.entities.Status;

import lombok.Data;

@Data
public class CourseHistoryDto {

    private Long id;
    private String code;
    private String title;
    private Integer units;
    private Semester semesterTaken;
    private Integer yearTaken;
    private BigDecimal grade;
    private Status status;
    private Integer courseAttempt;
}
