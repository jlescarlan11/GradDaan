package com.graddaan.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.graddaan.backend.dtos.CourseDto;
import com.graddaan.backend.entities.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDto toDto(Course course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isGe", ignore = true)
    @Mapping(target = "semesterOffered", ignore = true)
    Course toEntity(CourseDto courseDto);
}
