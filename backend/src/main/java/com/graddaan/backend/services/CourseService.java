package com.graddaan.backend.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.graddaan.backend.dtos.CourseDto;
import com.graddaan.backend.entities.Course;
import com.graddaan.backend.entities.Prerequisite;
import com.graddaan.backend.entities.SemesterOffered;
import com.graddaan.backend.mappers.CourseMapper;
import com.graddaan.backend.repositories.CourseRepository;
import com.graddaan.backend.repositories.PrerequisiteRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final PrerequisiteRepository prerequisiteRepository;

    public List<CourseDto> getAllCourses(String code, String title, Integer units, Boolean isGe, SemesterOffered semesterOffered) {
        List<Course> courses = courseRepository.findCoursesWithFilters(code, title, units, isGe, semesterOffered);

        return courses
                .stream()
                .map(courseMapper::toDto)
                .toList();
    }


    /**
     * Method to get course prerequisites (direct + indirect)
     */
    public List<CourseDto> getCoursePrerequisites(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Course not found with id: " + courseId);
        }
        
        Set<Long> visitedCourses = new HashSet<>();
        List<CourseDto> allPrerequisites = new ArrayList<>();
        
        // Get the complete prerequisite chain recursively
        getPrerequisiteChain(courseId, visitedCourses, allPrerequisites);
        
        return allPrerequisites;
    }
    
    /**
     * Recursively gets all prerequisites for a course
     * @param courseId - the course to get prerequisites for
     * @param visitedCourses - set to track visited courses (prevent circular dependencies)
     * @param allPrerequisites - list to store all found prerequisites
     */
    private void getPrerequisiteChain(Long courseId, Set<Long> visitedCourses, List<CourseDto> allPrerequisites) {
        // Prevent infinite loops (circular dependencies)
        if (visitedCourses.contains(courseId)) {
            return;
        }
        
        visitedCourses.add(courseId);
        
        // Get direct prerequisites for this course
        List<Prerequisite> prerequisites = prerequisiteRepository.findByCourseId(courseId);
        
        for (Prerequisite prereq : prerequisites) {
            Course prerequisiteCourse = prereq.getPrerequisiteCourse();
            CourseDto prerequisiteDto = courseMapper.toDto(prerequisiteCourse);
            
            // Check if we already added this prerequisite (avoid duplicates)
            boolean alreadyExists = allPrerequisites.stream()
                .anyMatch(course -> course.getId().equals(prerequisiteDto.getId()));
            
            if (!alreadyExists) {
                allPrerequisites.add(prerequisiteDto);
                
                // Recursively get prerequisites of this prerequisite
                getPrerequisiteChain(prerequisiteCourse.getId(), 
                                   new HashSet<>(visitedCourses), 
                                   allPrerequisites);
            }
        }
    }
}
