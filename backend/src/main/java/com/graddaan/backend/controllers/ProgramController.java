package com.graddaan.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.graddaan.backend.dtos.CourseDto;
import com.graddaan.backend.dtos.ProgramDto;
import com.graddaan.backend.services.ProgramCourseService;
import com.graddaan.backend.services.ProgramService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramService programService;
    private final ProgramCourseService programCourseService; // Add this dependency

    @GetMapping
    public ResponseEntity<List<ProgramDto>> getAllPrograms() {
        return ResponseEntity.ok(programService.getAllPrograms());
    }

    @GetMapping("/{programId}/courses")
    public ResponseEntity<List<CourseDto>> getProgramCourses(@PathVariable Long programId) {
        return ResponseEntity.ok(programCourseService.getProgramCourses(programId));
    }
}
