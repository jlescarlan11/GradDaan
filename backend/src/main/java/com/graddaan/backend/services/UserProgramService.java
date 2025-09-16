package com.graddaan.backend.services;

import org.springframework.stereotype.Service;

import com.graddaan.backend.dtos.UserProgramDto;
import com.graddaan.backend.entities.UserProgram;
import com.graddaan.backend.entities.UserProgramId;
import com.graddaan.backend.mappers.UserProgramMapper;
import com.graddaan.backend.repositories.ProgramRepository;
import com.graddaan.backend.repositories.UserProgramRepository;
import com.graddaan.backend.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserProgramService {

    private final UserProgramRepository userProgramRepository;
    private final UserRepository userRepository;
    private final ProgramRepository programRepository;
    private final UserProgramMapper userProgramMapper;

    public UserProgramDto enrollUserInProgram(Long userId, Long programId) {
        // Check if user exists
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        // Check if program exists
        if (!programRepository.existsById(programId)) {
            throw new RuntimeException("Program not found with id: " + programId);
        }

        // Check if enrollment already exists
        UserProgramId userProgramId = new UserProgramId(userId, programId);
        if (userProgramRepository.existsById(userProgramId)) {
            throw new RuntimeException("User is already enrolled in this program");
        }

        // Create new enrollment
        UserProgram userProgram = new UserProgram();
        userProgram.setUserId(userId);
        userProgram.setProgramId(programId);
        userProgram.setIsCurrent(true);

        userProgram = userProgramRepository.save(userProgram);

        return userProgramMapper.toDto(userProgram);
    }
}
