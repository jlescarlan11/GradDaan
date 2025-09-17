package com.graddaan.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.graddaan.backend.dtos.UserCourseDto;
import com.graddaan.backend.entities.UserCourse;

@Mapper(componentModel = "spring")
public interface UserCourseMapper {

    UserCourseDto toDto(UserCourse userCourse);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "attemptNumber", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserCourse toEntity(UserCourseDto userCourseDto);
}
