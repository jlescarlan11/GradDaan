package com.graddaan.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.graddaan.backend.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public boolean existsByEmail(String email);

    public boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

}
