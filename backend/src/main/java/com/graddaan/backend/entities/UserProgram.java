package com.graddaan.backend.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_programs")
@IdClass(UserProgramId.class)
public class UserProgram {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "program_id")
    private Long programId;

    @Column(name = "is_current")
    private Boolean isCurrent = true;

    @Column(name = "enrolled_at", insertable = false, updatable = false)
    private LocalDate enrolledAt;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "program_id", insertable = false, updatable = false)
    private Program program;
}
