package com.sni.hairsalon.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class BulkScheduleRequestDTO {
    private String barberId;
    private List<Integer> workingDays;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isRecurring;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}