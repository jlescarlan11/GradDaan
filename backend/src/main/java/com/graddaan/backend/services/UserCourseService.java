package com.graddaan.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.graddaan.backend.dtos.UserCourseDto;
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

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final UserCourseRepository userCourseRepository;
    private final UserCourseMapper userCourseMapper;

    public UserCourseDto addCompletedCourse(UserCourseDto request) {
        if (!userRepository.existsById(request.getUserId())) {
            throw new RuntimeException("User not found with id: " + request.getUserId());
        }

        if (!courseRepository.existsById(request.getCourseId())) {
            throw new RuntimeException("Coure not found with id: " + request.getCourseId());
        }

        UserCourse userCourse = userCourseMapper.toEntity(request);

        List<UserCourse> userCourseAttempts = userCourseRepository.findAllByUserIdAndCourseId(request.getUserId(), request.getCourseId());
        userCourse.setAttemptNumber(userCourseAttempts.size() + 1);

        Status status = request.getGrade().compareTo(new java.math.BigDecimal("3.0")) <= 0 ? Status.PASSED : Status.FAILED;
        userCourse.setStatus(status);

        userCourse = userCourseRepository.save(userCourse);

        return userCourseMapper.toDto(userCourse);

    }
}
