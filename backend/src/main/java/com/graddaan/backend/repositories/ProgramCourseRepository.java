package com.graddaan.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.graddaan.backend.entities.ProgramCourse;
import com.graddaan.backend.entities.ProgramCourseId;

@Repository
public interface ProgramCourseRepository extends JpaRepository<ProgramCourse, ProgramCourseId> {

    List<ProgramCourse> findAllByProgramId(Long programId);
}
