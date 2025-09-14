package com.graddaan.backend.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.graddaan.backend.dtos.RegisterUserDto;
import com.graddaan.backend.dtos.UserDto;
import com.graddaan.backend.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(RegisterUserDto registerUserDto);
}
