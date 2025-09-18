package com.graddaan.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.graddaan.backend.entities.UserCourse;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {

    List<UserCourse> findAllByUserIdAndCourseId(Long userId, Long courseId);

    List<UserCourse> findAllByUserId(Long userId);
}
