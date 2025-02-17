package com.sni.hairsalon.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleResponseDTO {
    private String id;
    private String barberId;
    private int dayOfWeek; 
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isRecurring;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    
    
}
