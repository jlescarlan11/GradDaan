package com.graddaan.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.graddaan.backend.dtos.CourseDto;
import com.graddaan.backend.entities.ProgramCourse;
import com.graddaan.backend.mappers.CourseMapper;
import com.graddaan.backend.repositories.ProgramCourseRepository;
import com.graddaan.backend.repositories.ProgramRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProgramCourseService {

    private final ProgramRepository programRepository;
    private final ProgramCourseRepository programCourseRepository;
    private final CourseMapper courseMapper;

    public List<CourseDto> getProgramCourses(Long programId) {
        // First check if program exists
        if (!programRepository.existsById(programId)) {
            throw new RuntimeException("Program not found with id: " + programId);
        }

        List<ProgramCourse> programCourses = programCourseRepository.findAllByProgramId(programId);

        return programCourses
                .stream()
                .map(ProgramCourse::getCourse) // Extract the Course entity
                .map(courseMapper::toDto) // Map Course to CourseDto
                .toList();
    }
}
