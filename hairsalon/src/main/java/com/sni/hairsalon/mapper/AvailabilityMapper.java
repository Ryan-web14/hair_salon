package com.sni.hairsalon.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.AvailabilitRequestDTO;
import com.sni.hairsalon.dto.response.AvailabilityResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.Availability;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.repository.BarberRepository;

@Component
public class AvailabilityMapper {
    
    @Autowired
    BarberRepository barberRepo;

    public Availability toEntity(AvailabilitRequestDTO dto){
        Barber barber = barberRepo.findBarberById(dto.getBarberId())
        .orElseThrow(()-> new ResourceNotFoundException("Barber not found"));
        Availability availability = new Availability();
        availability.setBarber(barber);
        availability.setStartTime(dto.getStarTime());
        availability.setEndTime(dto.getEndTime());
        availability.set_available(dto.isAvailable());
        
        if(dto.getNote() != null || !dto.getNote().isEmpty()){
            availability.setNote(dto.getNote());
        }
        
        return availability;
    }

    public AvailabilityResponseDTO toDto(Availability entity){
        AvailabilityResponseDTO dto = new AvailabilityResponseDTO();
        dto.setId(entity.getId());
        dto.setBarberId(entity.getBarber().getId());
        dto.setStarTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        
        if(entity.getNote() != null || !entity.getNote().isEmpty()){
            dto.setNote(entity.getNote());
        }

        return dto;
    }

}