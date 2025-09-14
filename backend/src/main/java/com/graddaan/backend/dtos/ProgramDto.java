package com.graddaan.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProgramDto {

    @NotBlank(message = "Code is required")
    @Size(max = 10, message = "Code  must be less than 10 characters")
    private String code;

    @NotBlank(message = "Code is required")
    @Size(max = 255, message = "Code  must be less than 255 characters")
    private String name;

    @NotBlank(message = "Total units is required")
    private Integer totalUnits;

    @NotBlank(message = "Total ge elective units is required")
    private Integer totalGeElectiveUnits;
}
