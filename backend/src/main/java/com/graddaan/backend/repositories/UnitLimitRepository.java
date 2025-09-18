package com.graddaan.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.graddaan.backend.entities.UnitLimit;
import com.graddaan.backend.entities.UnitLimitId;

@Repository
public interface UnitLimitRepository extends JpaRepository<UnitLimit, UnitLimitId> {

    List<UnitLimit> findByProgramIdOrderByYearLevelAscSemesterAsc(Long programId);

}
