package com.graddaan.backend.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.graddaan.backend.dtos.GraduationStatusDto;
import com.graddaan.backend.entities.Program;
import com.graddaan.backend.entities.ProgramCourse;
import com.graddaan.backend.entities.Status;
import com.graddaan.backend.entities.UnitLimit;
import com.graddaan.backend.entities.User;
import com.graddaan.backend.entities.UserCourse;
import com.graddaan.backend.repositories.ProgramCourseRepository;
import com.graddaan.backend.repositories.ProgramRepository;
import com.graddaan.backend.repositories.UnitLimitRepository;
import com.graddaan.backend.repositories.UserCourseRepository;
import com.graddaan.backend.repositories.UserProgramRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GraduationStatusService {

    private static final int DEFAULT_UNITS_PER_SEMESTER = 18;

    private final UserCourseRepository userCourseRepository;
    private final UserProgramRepository userProgramRepository;
    private final ProgramCourseRepository programCourseRepository;
    private final ProgramRepository programRepository;
    private final UnitLimitRepository unitLimitRepository;

    public GraduationStatusDto getGraduationStatus(User user) {
        Long programId = getCurrentProgramId(user);
        Program program = getProgram(programId);

        List<UserCourse> passedCourses = userCourseRepository.findByUserIdAndStatus(user.getId(), Status.PASSED);
        List<ProgramCourse> requiredCourses = programCourseRepository.findByProgramIdAndIsRequired(programId, true);

        int completedUnits = getTotalUnits(passedCourses);
        int totalRequiredUnits = getTotalUnits(requiredCourses);
        int completedRequiredUnits = calculateCompletedRequiredUnits(requiredCourses, passedCourses);

        int remainingRequiredUnits = Math.max(0, totalRequiredUnits - completedRequiredUnits);
        int totalElectiveUnits = program.getTotalUnits() - totalRequiredUnits;
        int completedElectiveUnits = completedUnits - completedRequiredUnits;
        int remainingElectiveUnits = Math.max(0, totalElectiveUnits - completedElectiveUnits);

        int totalRemainingUnits = remainingRequiredUnits + remainingElectiveUnits;
        String estimatedGraduation = calculateEstimatedGraduation(totalRemainingUnits, completedUnits, programId);

        return GraduationStatusDto.builder()
                .completedUnits(completedUnits)
                .requiredUnits(program.getTotalUnits())
                .remainingRequiredUnits(remainingRequiredUnits)
                .remainingElectiveUnits(remainingElectiveUnits)
                .estimatedGraduation(estimatedGraduation)
                .build();
    }

    private Long getCurrentProgramId(User user) {
        return userProgramRepository.findByUserIdAndIsCurrent(user.getId(), true)
                .orElseThrow(() -> new IllegalStateException("User has no current program"))
                .getProgramId();
    }

    private Program getProgram(Long programId) {
        return programRepository.findById(programId)
                .orElseThrow(() -> new EntityNotFoundException("Program not found"));
    }

    private int getTotalUnits(List<?> courses) {
        return courses.stream()
                .mapToInt(this::getUnitsFromCourse)
                .sum();
    }

    private int getUnitsFromCourse(Object course) {
        if (course instanceof UserCourse userCourse) {
            return userCourse.getCourse().getUnits();
        }
        if (course instanceof ProgramCourse programCourse) {
            return programCourse.getCourse().getUnits();
        }
        return 0;
    }

    private int calculateCompletedRequiredUnits(List<ProgramCourse> requiredCourses, List<UserCourse> passedCourses) {
        Set<Long> completedCourseIds = passedCourses.stream()
                .map(UserCourse::getCourseId)
                .collect(Collectors.toSet());

        return requiredCourses.stream()
                .filter(course -> completedCourseIds.contains(course.getCourseId()))
                .mapToInt(course -> course.getCourse().getUnits())
                .sum();
    }

    private String calculateEstimatedGraduation(int remainingUnits, int completedUnits, Long programId) {
        if (remainingUnits <= 0) {
            return "Ready to graduate";
        }

        List<UnitLimit> unitLimits = unitLimitRepository.findByProgramIdOrderByYearLevelAscSemesterAsc(programId);

        if (unitLimits.isEmpty()) {
            return calculateSimpleEstimation(remainingUnits);
        }

        int semestersNeeded = calculateSemestersWithLimits(remainingUnits, completedUnits, unitLimits);
        return formatSemestersRemaining(semestersNeeded);
    }

    private int calculateSemestersWithLimits(int remainingUnits, int completedUnits, List<UnitLimit> unitLimits) {
        int currentSemesterIndex = findCurrentSemesterIndex(completedUnits, unitLimits);
        int unitsLeft = remainingUnits;
        int semestersNeeded = 0;

        // Calculate remaining capacity in current semester
        if (currentSemesterIndex < unitLimits.size()) {
            UnitLimit currentLimit = unitLimits.get(currentSemesterIndex);
            int unitsInCurrentSemester = getUnitsInCurrentSemester(completedUnits, currentSemesterIndex, unitLimits);
            int remainingCapacity = currentLimit.getMaxUnits() - unitsInCurrentSemester;

            if (remainingCapacity > 0 && unitsLeft > 0) {
                unitsLeft -= Math.min(unitsLeft, remainingCapacity);
                semestersNeeded = 1;
            }
        }

        // Calculate future semesters needed
        int nextSemesterIndex = currentSemesterIndex + 1;
        while (unitsLeft > 0) {
            UnitLimit limit = nextSemesterIndex < unitLimits.size()
                    ? unitLimits.get(nextSemesterIndex)
                    : unitLimits.get(unitLimits.size() - 1);

            unitsLeft -= Math.min(unitsLeft, limit.getMaxUnits());
            semestersNeeded++;
            nextSemesterIndex++;
        }

        return semestersNeeded;
    }

    private int findCurrentSemesterIndex(int completedUnits, List<UnitLimit> unitLimits) {
        int accumulatedUnits = 0;

        for (int i = 0; i < unitLimits.size(); i++) {
            if (accumulatedUnits + unitLimits.get(i).getMaxUnits() > completedUnits) {
                return i;
            }
            accumulatedUnits += unitLimits.get(i).getMaxUnits();
        }

        return unitLimits.size(); // Completed all defined semesters
    }

    private int getUnitsInCurrentSemester(int completedUnits, int currentSemesterIndex, List<UnitLimit> unitLimits) {
        int unitsBeforeCurrentSemester = 0;

        for (int i = 0; i < currentSemesterIndex; i++) {
            unitsBeforeCurrentSemester += unitLimits.get(i).getMaxUnits();
        }

        return completedUnits - unitsBeforeCurrentSemester;
    }

    private String calculateSimpleEstimation(int remainingUnits) {
        int semestersRemaining = (int) Math.ceil((double) remainingUnits / DEFAULT_UNITS_PER_SEMESTER);
        return formatSemestersRemaining(semestersRemaining);
    }

    private String formatSemestersRemaining(int semesters) {
        return switch (semesters) {
            case 0 ->
                "Ready to graduate";
            case 1 ->
                "1 semester remaining";
            default ->
                semesters + " semesters remaining";
        };
    }
}
