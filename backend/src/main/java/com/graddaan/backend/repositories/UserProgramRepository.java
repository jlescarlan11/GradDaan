package com.graddaan.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.graddaan.backend.entities.UserProgram;
import com.graddaan.backend.entities.UserProgramId;

@Repository
public interface UserProgramRepository extends  JpaRepository<UserProgram, UserProgramId> {

}
