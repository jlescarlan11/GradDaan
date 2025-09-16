package com.graddaan.backend.dtos;

import lombok.Data;

@Data
public class CourseDto {

    private String id;
    private String code;
    private String title;
    private Integer units;
}
