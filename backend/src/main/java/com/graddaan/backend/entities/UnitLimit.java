package com.graddaan.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "unit_limits")
@IdClass(UnitLimitId.class)
public class UnitLimit {

    @Id
    @Column(name = "program_id")
    private Long programId;

    @Id
    @Column(name = "year_level")
    private Integer yearLevel;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "semester")
    private Semester semester;

    @Column(name = "max_units", nullable = false)
    private Integer maxUnits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", insertable = false, updatable = false)
    private Program program;
}
