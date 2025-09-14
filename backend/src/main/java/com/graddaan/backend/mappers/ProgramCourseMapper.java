package com.graddaan.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.graddaan.backend.dtos.ProgramCourseDto;
import com.graddaan.backend.entities.ProgramCourse;

@Mapper(componentModel = "spring")
public interface ProgramCourseMapper {

    ProgramCourseDto toDto(ProgramCourse programCourse);

    @Mapping(target = "program", ignore = true)
    @Mapping(target = "course", ignore = true)
    ProgramCourse toEntity(ProgramCourseDto programCourseDto);
}
