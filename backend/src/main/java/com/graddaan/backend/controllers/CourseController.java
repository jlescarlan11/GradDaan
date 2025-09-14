package com.graddaan.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.graddaan.backend.dtos.CourseDto;
import com.graddaan.backend.entities.SemesterOffered;
import com.graddaan.backend.services.CourseService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer units,
            @RequestParam(required = false) Boolean isGe,
            @RequestParam(required = false) SemesterOffered semesterOffered) {

        return ResponseEntity.ok(courseService.getAllCourses(code, title, units, isGe, semesterOffered));
    }
}
