package com.graddaan.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.graddaan.backend.dtos.ProgramDto;
import com.graddaan.backend.mappers.ProgramMapper;
import com.graddaan.backend.repositories.ProgramRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;

    public List<ProgramDto> getAllPrograms() {
        return programRepository.findAll()
                .stream()
                .map(programMapper::toDto)
                .toList();
    }
}
