package com.graddaan.backend.dtos;

import com.graddaan.backend.entities.Semester;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UnitLimitDto {

    @NotNull(message = "Program ID is required")
    private Long programId;

    @NotNull(message = "Year level is required")
    @Min(value = 1)
    private Integer yearLevel;

    @NotNull(message = "Semester is required")
    private Semester semester;

    @NotNull(message = "Max units is required")
    @Min(value = 1)
    private Integer maxUnits;
}
