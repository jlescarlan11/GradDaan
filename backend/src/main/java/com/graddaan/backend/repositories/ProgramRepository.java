package com.graddaan.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.graddaan.backend.entities.Program;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    Optional<Program> findByCode(String code);
}
