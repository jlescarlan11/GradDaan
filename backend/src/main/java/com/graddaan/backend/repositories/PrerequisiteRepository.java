package com.graddaan.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.graddaan.backend.entities.Prerequisite;
import com.graddaan.backend.entities.PrerequisiteId;


public interface  PrerequisiteRepository extends JpaRepository<Prerequisite, PrerequisiteId> {
    List<Prerequisite> findByCourseId(Long courseId);
}
