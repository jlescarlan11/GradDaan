package com.graddaan.backend.dtos;

import lombok.Data;

@Data
public class ProgramCourseDto {

    private Long programId;
    private Long courseId;
    private Boolean isRequired;
}
