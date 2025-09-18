package com.graddaan.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.graddaan.backend.entities.UserProgram;
import com.graddaan.backend.entities.UserProgramId;

@Repository
public interface UserProgramRepository extends JpaRepository<UserProgram, UserProgramId> {

    // Find user's current program
    Optional<UserProgram> findByUserIdAndIsCurrent(Long userId, Boolean isCurrent);

    // Find specific user program
    Optional<UserProgram> findByUserIdAndProgramId(Long userId, Long programId);
}
