package com.graddaan.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.graddaan.backend.dtos.UserProgramDto;
import com.graddaan.backend.entities.UserProgram;

@Mapper(componentModel = "spring")
public interface UserProgramMapper {

    UserProgramDto toDto(UserProgram userProgram);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "program", ignore = true)
    @Mapping(target = "isCurrent", ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    UserProgram toEntity(UserProgramDto userProgramDto);
}
