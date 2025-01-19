package com.sni.hairsalon.service.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.AvailabilityRequestDTO;
import com.sni.hairsalon.dto.response.AvailabilityResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.AvailabilityMapper;
import com.sni.hairsalon.model.Availability;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.repository.AvailabilityRepository;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.service.AvailabilityService;

import jakarta.transaction.Transactional;
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
    @Transactional
    public List<AvailabilityResponseDTO> createAvailability(AvailabilityRequestDTO dto){
        log.debug("Creating availability slots for baber id {]", dto.getBarberId());

        Barber barber = barberRepo.findById(dto.getBarberId())
        .orElseThrow(()-> new ResourceNotFoundException("Barber not found"));

        List<Availability> slots = generateTimeSlot(barber, dto.getStarTime(), dto.getEndTime());

        List<AvailabilityResponseDTO> savedSlots = new ArrayList<>();
        int batchSize = 50;
        
        for (int i = 0; i < slots.size(); i += batchSize) {
            int end = Math.min(i + batchSize, slots.size());
            List<Availability> batch = slots.subList(i, end);
            
            List<Availability> savedBatch = availabilityRepo.saveAll(batch);
            savedSlots.addAll(savedBatch.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList()));
        }
        
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

        @Override
        public List<AvailabilityResponseDTO>getBarberAvailability(long barberId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); //.truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        List<Availability> slots = availabilityRepo
            .findByBarberIdAndDateAndIsAvailableTrue(
                barberId, 
                startOfDay,
                endOfDay
            );

            if(slots.isEmpty()){
                throw new ResourceNotFoundException("no availability");
            }

        return slots
        .stream()
        .map(availability->mapper.toDto(availability))
        .collect(Collectors.toList());
    }

    @Override
    public void makeSlotUnavailable(long barberId, LocalDateTime startTime, int duration){
        
        LocalDateTime endTime = startTime.plusMinutes(duration).truncatedTo(ChronoUnit.MINUTES);
        Availability availability = availabilityRepo.findByStartAndEndTimeAndBarber(barberId, 
        startTime.truncatedTo(ChronoUnit.MINUTES), endTime)
        .orElseThrow(()-> new ResourceNotFoundException("time slot not found"));

        availability.setAvailable(false);

        return;
    }


    @Override
    public Boolean isAvailableSlot(long barberId, LocalDateTime startTime, int duration){

        LocalDateTime endTime = startTime.plusMinutes(duration).truncatedTo(ChronoUnit.MINUTES);
        Availability availability = availabilityRepo.findByStartAndEndTimeAndBarber(barberId, 
        startTime.truncatedTo(ChronoUnit.MINUTES), endTime)
        .orElseThrow(()-> new ResourceNotFoundException("time slot not found"));
        return availability.isAvailable();
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
            .note(" ")
            .build();

            slots.add(slot);
            currentSlotStart = currentSlotStart.plusMinutes(30);
        }
        return slots;
    }

}