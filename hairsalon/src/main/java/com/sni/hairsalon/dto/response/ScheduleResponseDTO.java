package com.sni.hairsalon.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class ScheduleResponseDTO {
    private String id;
    private String barberId;
    private String esthecianId;
    private int dayOfWeek; 
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isRecurring;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    
    
}
