package com.graddaan.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.graddaan.backend.dtos.ProgramDto;
import com.graddaan.backend.entities.Program;

@Mapper(componentModel = "spring")
public interface ProgramMapper {

    ProgramDto toDto(Program program);

    @Mapping(target = "id", ignore = true)
    Program toEntity(ProgramDto programDto);
}
