package com.sni.hairsalon.mapper;

import java.sql.Date;

import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.ScheduleRequestDTO;
import com.sni.hairsalon.dto.response.ScheduleResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.Schedule;
import com.sni.hairsalon.repository.BarberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduleMapper {
    
   
   private final BarberRepository barberRepo;

    public Schedule toEntity(ScheduleRequestDTO dto){
        Barber barber = barberRepo.findBarberById(Long.parseLong(dto.getId())).
        orElseThrow(()-> new ResourceNotFoundException("Barber not found")); 
        Date sqlEffectiveFromDate = Date.valueOf(dto.getEffectiveFrom());
        Date sqlEffectiveToDate = Date.valueOf(dto.getEffectiveTo());
        Schedule schedule = Schedule.builder()
        .barber(barber)
        .dayOfWeek(dto.getDayOfWeek())
        .startTime(dto.getStartTime())
        .endTime(dto.getEndTime())
        .is_recurring(true)
        .effectiveFrom(sqlEffectiveFromDate)
        .effectiveTo(sqlEffectiveToDate)
        .build();
        return schedule;
    }

    public ScheduleResponseDTO toDto(Schedule schedule){
        ScheduleResponseDTO response = ScheduleResponseDTO.builder()
        .id(String.valueOf(schedule.getId()))
        .barberId(String.valueOf(schedule.getBarber().getId()))
        .dayOfWeek(schedule.getDayOfWeek())
        .startTime(schedule.getStartTime())
        .endTime(schedule.getEndTime())
        .isRecurring(schedule.is_recurring())
        .effectiveFrom(schedule.getEffectiveFrom().toLocalDate())
        .effectiveTo(schedule.getEffectiveTo().toLocalDate())
        .build();
        return response;
    }
}
