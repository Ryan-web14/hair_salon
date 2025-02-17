package com.sni.hairsalon.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleTemplateRequestDTO {
       private String barberId;
    private List<Integer> workingDays;  
    private LocalTime startTime;        
    private LocalTime endTime;          
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
