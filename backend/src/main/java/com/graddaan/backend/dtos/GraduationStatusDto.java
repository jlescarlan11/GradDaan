package com.graddaan.backend.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraduationStatusDto {

    private Integer completedUnits;
    private Integer requiredUnits;
    private Integer remainingRequiredUnits;
    private Integer remainingElectiveUnits;
    private String estimatedGraduation;
}
