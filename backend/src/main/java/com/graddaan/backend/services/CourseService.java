package com.graddaan.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.graddaan.backend.dtos.CourseDto;
import com.graddaan.backend.entities.Course;
import com.graddaan.backend.entities.SemesterOffered;
import com.graddaan.backend.mappers.CourseMapper;
import com.graddaan.backend.repositories.CourseRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public List<CourseDto> getAllCourses(String code, String title, Integer units, Boolean isGe, SemesterOffered semesterOffered) {
        List<Course> courses = courseRepository.findCoursesWithFilters(code, title, units, isGe, semesterOffered);

        return courses
                .stream()
                .map(courseMapper::toDto)
                .toList();
    }
}
