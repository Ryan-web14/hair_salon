package com.sni.hairsalon.dto.request;

import java.time.LocalDateTime;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ScheduleRequestDTO {
    private String barberId;
    private int dayOfWeek; 
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isRecurring;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    
}
