package com.sni.hairsalon.mapper;

import java.sql.Date;

import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.ScheduleRequestDTO;
import com.sni.hairsalon.dto.response.ScheduleResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.Esthetician;
import com.sni.hairsalon.model.Schedule;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.repository.EstheticianRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduleMapper {
    
    private final BarberRepository barberRepo;
    private final EstheticianRepository estheticianRepo;

    public Schedule toEntity(ScheduleRequestDTO dto) {
        Schedule.ScheduleBuilder scheduleBuilder = Schedule.builder()
            .dayOfWeek(dto.getDayOfWeek())
            .startTime(dto.getStartTime())
            .endTime(dto.getEndTime())
            .is_recurring(dto.isRecurring())
            .effectiveFrom(Date.valueOf(dto.getEffectiveFrom()))
            .effectiveTo(Date.valueOf(dto.getEffectiveTo()));
        
        // Check if barberId (id) is provided
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            Barber barber = barberRepo.findBarberById(Long.parseLong(dto.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found")); 
            scheduleBuilder.barber(barber);
        } 
        // Check if estheticianId is provided
        else if (dto.getEstheticianId() != null && !dto.getEstheticianId().isEmpty()) {
            Esthetician esthetician = estheticianRepo.findEstheticianById(Long.parseLong(dto.getEstheticianId()))
                .orElseThrow(() -> new ResourceNotFoundException("Esthetician not found"));
            scheduleBuilder.esthetician(esthetician);
        } else {
            throw new IllegalArgumentException("Either barber id or esthetician id must be provided");
        }
        
        return scheduleBuilder.build();
    }

    public ScheduleResponseDTO toDto(Schedule schedule) {
        ScheduleResponseDTO responseBuilder = ScheduleResponseDTO.builder()
            .id(String.valueOf(schedule.getId()))
            .dayOfWeek(schedule.getDayOfWeek())
            .startTime(schedule.getStartTime())
            .endTime(schedule.getEndTime())
            .isRecurring(schedule.is_recurring())
            .effectiveFrom(schedule.getEffectiveFrom().toLocalDate())
            .effectiveTo(schedule.getEffectiveTo().toLocalDate())
            .build();
        
        if (schedule.getBarber() != null) {
            responseBuilder.setBarberId(String.valueOf(schedule.getBarber().getId()));
        } 
        
        if (schedule.getEsthetician() != null) {
            responseBuilder.setEsthecianId(String.valueOf(schedule.getEsthetician().getId()));
        }
        
        return responseBuilder;
    }
}