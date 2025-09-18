package com.graddaan.backend.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.graddaan.backend.dtos.CourseHistoryDto;
import com.graddaan.backend.dtos.UserCourseDto;
import com.graddaan.backend.dtos.UserCourseUpdateDto;
import com.graddaan.backend.entities.Course;
import com.graddaan.backend.entities.Status;
import com.graddaan.backend.entities.UserCourse;
import com.graddaan.backend.mappers.UserCourseMapper;
import com.graddaan.backend.repositories.CourseRepository;
import com.graddaan.backend.repositories.UserCourseRepository;
import com.graddaan.backend.repositories.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserCourseService {

    private static final BigDecimal PASSING_GRADE_THRESHOLD = new BigDecimal("3.0");

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final UserCourseRepository userCourseRepository;
    private final UserCourseMapper userCourseMapper;

    public UserCourseDto addCompletedCourse(UserCourseDto request) {
        validateUserExists(request.getUserId());
        validateCourseExists(request.getCourseId());

        List<UserCourse> userCourseAttempts = userCourseRepository.findAllByUserIdAndCourseId(
                request.getUserId(), request.getCourseId());

        if (hasPassedAttempt(userCourseAttempts)) {
            throw new RuntimeException("User has already passed this course. Course ID: " + request.getCourseId());
        }

        UserCourse userCourse = userCourseMapper.toEntity(request);
        userCourse.setAttemptNumber(userCourseAttempts.size() + 1);
        userCourse.setStatus(calculateStatus(request.getGrade()));

        return userCourseMapper.toDto(userCourseRepository.save(userCourse));
    }

    public List<CourseHistoryDto> getUserCourseHistory(Long userId) {
        return userCourseRepository.findAllByUserId(userId).stream()
                .map(this::convertToCourseHistoryDto)
                .collect(Collectors.toList());
    }

    public CourseHistoryDto updateCourseRecord(Long userCourseId, UserCourseUpdateDto updateData) {
        UserCourse userCourse = userCourseRepository.findById(userCourseId)
                .orElseThrow(() -> new RuntimeException("Course record not found with id: " + userCourseId));

        if (updateData.getGrade() != null) {
            updateGradeAndStatus(userCourse, updateData.getGrade(), userCourseId);
        }

        if (updateData.getSemesterTaken() != null) {
            userCourse.setSemesterTaken(updateData.getSemesterTaken());
        }

        if (updateData.getYearTaken() != null) {
            userCourse.setYearTaken(updateData.getYearTaken());
        }

        return convertToCourseHistoryDto(userCourseRepository.save(userCourse));
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

    private void validateCourseExists(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Course not found with id: " + courseId);
        }
    }

    private boolean hasPassedAttempt(List<UserCourse> attempts) {
        return attempts.stream().anyMatch(attempt -> attempt.getStatus() == Status.PASSED);
    }

    private Status calculateStatus(BigDecimal grade) {
        return grade.compareTo(PASSING_GRADE_THRESHOLD) <= 0 ? Status.PASSED : Status.FAILED;
    }

    private void updateGradeAndStatus(UserCourse userCourse, BigDecimal newGrade, Long userCourseId) {
        Status newStatus = calculateStatus(newGrade);

        // Check for duplicate passed attempts only if changing to PASSED status
        if (newStatus == Status.PASSED && userCourse.getStatus() != Status.PASSED) {
            List<UserCourse> attempts = userCourseRepository.findAllByUserIdAndCourseId(
                    userCourse.getUserId(), userCourse.getCourseId());

            boolean hasOtherPassedAttempt = attempts.stream()
                    .anyMatch(attempt -> !attempt.getId().equals(userCourseId)
                    && attempt.getStatus() == Status.PASSED);

            if (hasOtherPassedAttempt) {
                throw new RuntimeException("User already has a passed attempt for this course. Cannot have multiple passed records.");
            }
        }

        userCourse.setGrade(newGrade);
        userCourse.setStatus(newStatus);
    }

    private CourseHistoryDto convertToCourseHistoryDto(UserCourse userCourse) {
        Course course = courseRepository.findById(userCourse.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + userCourse.getCourseId()));

        CourseHistoryDto dto = new CourseHistoryDto();
        dto.setId(userCourse.getId());
        dto.setCode(course.getCode());
        dto.setTitle(course.getTitle());
        dto.setUnits(course.getUnits());
        dto.setSemesterTaken(userCourse.getSemesterTaken());
        dto.setYearTaken(userCourse.getYearTaken());
        dto.setGrade(userCourse.getGrade());
        dto.setStatus(userCourse.getStatus());
        dto.setCourseAttempt(userCourse.getAttemptNumber());

        return dto;
    }
}
