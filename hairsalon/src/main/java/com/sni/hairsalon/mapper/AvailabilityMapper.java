package com.sni.hairsalon.mapper;

import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.AvailabilityRequestDTO;
import com.sni.hairsalon.dto.response.AvailabilityResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.Availability;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.repository.BarberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AvailabilityMapper {
    
    private final BarberRepository barberRepo;

    public Availability toEntity(AvailabilityRequestDTO dto){
        Barber barber = barberRepo.findBarberById(Long.parseLong(dto.getBarberId()))
        .orElseThrow(()-> new ResourceNotFoundException("Barber not found"));
        Availability availability = new Availability();
        availability.setBarber(barber);
        availability.setStartTime(dto.getStarTime());
        availability.setEndTime(dto.getEndTime());
        availability.setAvailable(dto.isAvailable());
        
        if(dto.getNote() != null  && !dto.getNote().isEmpty()){
            availability.setNote(dto.getNote());
        }
        
        return availability;
    }

    public AvailabilityResponseDTO toDto(Availability entity){
        AvailabilityResponseDTO dto = new AvailabilityResponseDTO();
        dto.setId(entity.getId());
        dto.setBarberId(String.valueOf(entity.getBarber().getId()));
        dto.setFirstname(entity.getBarber().getFirstname());
        dto.setLastName(entity.getBarber().getLastname());
        dto.setStarTime(entity.getStartTime().truncatedTo(ChronoUnit.MINUTES));
        dto.setEndTime(entity.getEndTime().truncatedTo(ChronoUnit.MINUTES));
        dto.setAvailable(entity.isAvailable());
        
        if(entity.getNote() != null || !entity.getNote().isEmpty()){
            dto.setNote(entity.getNote());
        }

        return dto;
    }

}
