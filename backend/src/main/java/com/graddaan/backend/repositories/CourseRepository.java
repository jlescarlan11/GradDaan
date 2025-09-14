package com.graddaan.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.graddaan.backend.entities.Course;
import com.graddaan.backend.entities.SemesterOffered;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE "
            + "(:code IS NULL OR LOWER(c.code) LIKE LOWER(CONCAT('%', :code, '%'))) AND "
            + "(:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND "
            + "(:units IS NULL OR c.units = :units) AND "
            + "(:isGe IS NULL OR c.isGe = :isGe) AND "
            + "(:semesterOffered IS NULL OR c.semesterOffered = :semesterOffered)")
    List<Course> findCoursesWithFilters(
            @Param("code") String code,
            @Param("title") String title,
            @Param("units") Integer units,
            @Param("isGe") Boolean isGe,
            @Param("semesterOffered") SemesterOffered semesterOffered
    );
}
