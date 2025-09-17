package com.graddaan.backend.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.graddaan.backend.dtos.RegisterUserDto;
import com.graddaan.backend.dtos.UserCourseDto;
import com.graddaan.backend.dtos.UserDto;
import com.graddaan.backend.dtos.UserProgramDto;
import com.graddaan.backend.entities.Role;
import com.graddaan.backend.mappers.UserMapper;
import com.graddaan.backend.repositories.UserRepository;
import com.graddaan.backend.services.UserCourseService;
import com.graddaan.backend.services.UserProgramService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserProgramService userProgramService;
    private final UserCourseService userCourseService;

    @PostMapping
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterUserDto request,
            UriComponentsBuilder uriBuilder
    ) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("username", "Username is already registered"));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("email", "Email is already registered"));
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user = userRepository.save(user);

        UserDto userDto = userMapper.toDto(user);
        var uri = uriBuilder.path("/api/users/{id}")
                .buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PostMapping("/enrollment")
    public ResponseEntity<UserProgramDto> enrollUserInProgram(
            @RequestParam Long userId,
            @RequestParam Long programId,
            UriComponentsBuilder uriBuilder
    ) {
        UserProgramDto userProgramDto = userProgramService.enrollUserInProgram(userId, programId);
        var uri = uriBuilder.path("/api/users/{userId}/programs/{programId}")
                .buildAndExpand(userProgramDto.getUserId(), userProgramDto.getProgramId()).toUri();
        return ResponseEntity.created(uri).body(userProgramDto);
    }

    @PostMapping("/courses")
    public ResponseEntity<UserCourseDto> addCompletedCourse(
            @Valid @RequestBody UserCourseDto request,
            UriComponentsBuilder uriBuilder
    ) {
        UserCourseDto userCourseDto = userCourseService.addCompletedCourse(request);
        var uri = uriBuilder.path("/api/users/{userId}/courses/{courseId}")
                .buildAndExpand(userCourseDto.getUserId(), userCourseDto.getCourseId()).toUri();
        return ResponseEntity.created(uri).body(userCourseDto);
    }
}
