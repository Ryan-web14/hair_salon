package com.sni.hairsalon.service.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.AvailabilitRequestDTO;
import com.sni.hairsalon.dto.response.AvailabilityResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.AvailabilityMapper;
import com.sni.hairsalon.model.Availability;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.repository.AvailabilityRepository;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.service.AvailabilityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvailabilityServiceImpl implements AvailabilityService {
    
    private final AvailabilityRepository availabilityRepo;
    private final BarberRepository barberRepo;
    private final AvailabilityMapper mapper;

    @Override
    public List<AvailabilityResponseDTO> createAvailability(AvailabilitRequestDTO dto){
        log.debug("Creating availability slots for baber id {]", dto.getBarberId());

        Barber barber = barberRepo.findById(dto.getBarberId())
        .orElseThrow(()-> new ResourceNotFoundException("Barber not found"));

        List<Availability> slots = generateTimeSlot(barber, dto.getStarTime(), dto.getEndTime());

    List<AvailabilityResponseDTO> savedSlots = availabilityRepo.saveAll(slots)
    .stream()
    .map(slot -> mapper.toDto(slot))
    .collect(Collectors.toList());
        
    return savedSlots; 
    }

    @Override
    public AvailabilityResponseDTO updateAvailabilityStatus(long id, boolean status){
        Availability updatedavailability = availabilityRepo.findById(id)
        .orElseThrow(()-> new ResourceNotFoundException("Availability not found "));
        updatedavailability.setAvailable(status);
        availabilityRepo.save(updatedavailability);
        return mapper.toDto(updatedavailability);
    }

    private List<Availability> generateTimeSlot(Barber barber, 
    LocalDateTime startTime, LocalDateTime endTime){
        List<Availability> slots = new ArrayList<>();
        LocalDateTime currentSlotStart = startTime;

        while(currentSlotStart.plusMinutes(30).isBefore(endTime) ||
        currentSlotStart.plusMinutes(30).equals(endTime)){
            Availability slot = Availability.builder()
            .barber(barber)
            .startTime(currentSlotStart)
            .endTime(currentSlotStart.plusMinutes(30))
            .isAvailable(true)
            .note(null)
            .build();

            slots.add(slot);
            currentSlotStart = currentSlotStart.plusMinutes(30);
        }
        return slots;
    }
        @Override
        public List<AvailabilityResponseDTO>getBarberAvailability(long barberId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        List<Availability> slots = availabilityRepo
            .findByBarberIdAndStartTimeBetweenAndIsAvailableTrue(
                barberId, 
                startOfDay, 
                endOfDay
            );

        return slots
        .stream()
        .map(availability->mapper.toDto(availability))
        .collect(Collectors.toList());
    }
}