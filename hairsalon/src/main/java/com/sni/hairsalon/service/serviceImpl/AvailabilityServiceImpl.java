package com.sni.hairsalon.service.serviceImpl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.AvailabilityRequestDTO;
import com.sni.hairsalon.dto.response.AvailabilityResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.AvailabilityMapper;
import com.sni.hairsalon.model.Availability;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.Schedule;
import com.sni.hairsalon.repository.AvailabilityRepository;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.repository.ScheduleRepository;
import com.sni.hairsalon.service.AvailabilityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//TODO Work on a asynchronous generation slot for large batch in big schedule 
@Service
@RequiredArgsConstructor
@Slf4j
public class AvailabilityServiceImpl implements AvailabilityService {

    
    private final AvailabilityRepository availabilityRepo;
    private final ScheduleRepository scheduleRepo;
    private final BarberRepository barberRepo;
    private final AvailabilityMapper mapper;

    @Override
    @Transactional
    public List<AvailabilityResponseDTO> createAvailability(AvailabilityRequestDTO dto){
        log.debug("Creating availability slots for baber id {]", dto.getBarberId());

        Barber barber = barberRepo.findById(Long.parseLong(dto.getBarberId()))
        .orElseThrow(()-> new ResourceNotFoundException("Barber not found"));

    List<Availability> generatedSlots = generateTimeSlot(barber, dto.getStarTime(), dto.getEndTime(), dto.getScheduleId());
    return generatedSlots.stream()
    .map(mapper::toDto)
    .collect(Collectors.toList()); 
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
public void makeSlotUnavailable(long barberId, LocalDateTime startTime, int duration) {
 
    startTime = startTime.truncatedTo(ChronoUnit.MINUTES);
    
    //1calculate each needed slots
    int slotDuration = 30; 
    int numSlots = (int) Math.ceil((double) duration / slotDuration);
    
    for (int i = 0; i < numSlots; i++) {
        LocalDateTime currentSlotStart = startTime.plusMinutes(i * slotDuration);
        LocalDateTime currentSlotEnd = currentSlotStart.plusMinutes(slotDuration);
        
        try {
            List<Availability> availabilities = availabilityRepo.findByStartAndEndTimeAndBarber(
                barberId, currentSlotStart, currentSlotEnd);
            
                    if(availabilities.isEmpty()){
                        throw new IllegalAccessError("Pas de disponibilité pour ce créneau");
                    }

                    Availability firstAvailability = availabilities.get(0);

            firstAvailability.setAvailable(false);
            availabilityRepo.save(firstAvailability);

            if(availabilities.size() > 1){
                List<Availability> duplicates = availabilities.subList(1, availabilities.size());
                availabilityRepo.deleteAll(duplicates);
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }
}

@Override
public void deleteAllAvailability(){

    availabilityRepo.deleteAll();
    return;
}

// //TODO fix those function to verify the availability of a slot
//     @Override
//     public Boolean isAvailableSlot(long barberId, LocalDateTime startTime, int duration){

//         LocalDateTime endTime = startTime.plusMinutes(duration).truncatedTo(ChronoUnit.MINUTES);
//         Availability availability = availabilityRepo.findByStartAndEndTimeAndBarber(barberId, 
//         startTime, endTime)
//         .orElseThrow(()-> new ResourceNotFoundException("time slot not found"));

//         return availability.isAvailable();
//     }

    // @Override
    // public Boolean isAvailableSlot(long barberId, LocalDateTime startTime, int duration) {
    //     startTime = startTime.truncatedTo(ChronoUnit.MINUTES);
    //     LocalDateTime endTime = startTime.plusMinutes(duration).truncatedTo(ChronoUnit.MINUTES);
        
     // Vérifier si nous avons des créneaux qui couvrent complètement la période
    //     List<Availability> overlappingSlots = availabilityRepo.findOverlappingSlots(
    //         barberId, startTime, endTime);
        
    //     if (overlappingSlots.isEmpty()) {
    //         return false;  // Pas de créneaux du tout
    //     }
        
        // Vérifier si tous les créneaux sont disponibles
    //     if (!overlappingSlots.stream().allMatch(Availability::isAvailable)) {
    //         return false;  // Au moins un créneau n'est pas disponible
    //     }
        
     // Vérifier si les créneaux couvrent toute la période
    //     LocalDateTime earliestStart = overlappingSlots.stream()
    //         .map(Availability::getStartTime)
    //         .min(LocalDateTime::compareTo)
    //         .orElse(LocalDateTime.MAX);
        
    //     LocalDateTime latestEnd = overlappingSlots.stream()
    //         .map(Availability::getEndTime)
    //         .max(LocalDateTime::compareTo)
    //         .orElse(LocalDateTime.MIN);
        
         // La période est entièrement couverte si le début des créneaux est <= au début demandé
         // et la fin des créneaux est >= à la fin demandée
    //     return !earliestStart.isAfter(startTime) && !latestEnd.isBefore(endTime);
    // }
    
    @Transactional
    private List<Availability> generateTimeSlot(Barber barber, 
    LocalDateTime startTime, LocalDateTime endTime, long scheduleId){
        List<Availability> slots = new ArrayList<>();
        LocalDateTime currentSlotStart = startTime.truncatedTo(ChronoUnit.MINUTES);

        validateWorkingHours(startTime, endTime, barber);

        //Availabilities are generated by batches
        int batch = 0;
        int maxBatchRequest = 20; 
        List<Availability> batchRequest = new ArrayList<>(maxBatchRequest); 

        try {
            //get any existing slot with same barber, start and endtime
            List<Availability> existingSlots = availabilityRepo.findByBarberAndTimeRangeOverlap(
                barber.getId(), startTime, endTime);
                
            if (!existingSlots.isEmpty()) {
                log.warn("Found {} overlapping slots", existingSlots.size());
                throw new IllegalStateException("Overlapping slots already exist in the database");
            }
            
            //Generate 30 minutes slots 
            while(currentSlotStart.plusMinutes(30).isBefore(endTime) || 
                  currentSlotStart.plusMinutes(30).equals(endTime)) {
                
                LocalDateTime slotStart = currentSlotStart;
                LocalDateTime slotEnd = currentSlotStart.plusMinutes(30);
                    
                boolean hasDuplicate = slots.stream()
                .anyMatch(slot -> 
                    slot.getStartTime().equals(slotStart) && 
                    slot.getEndTime().equals(slotEnd));
                    
            if (hasDuplicate) {
                log.warn("Duplicate slot detected at {}-{}", slotStart, slotEnd);
                currentSlotStart = currentSlotStart.plusMinutes(30);
                continue;
            }
                // Check for overlapping slots
                boolean hasOverlap = slots.stream()
                    .anyMatch(slot -> 
                        !slot.getEndTime().isBefore(slotStart) && 
                        !slot.getStartTime().isAfter(slotEnd));
    
                if (hasOverlap) {
                    throw new IllegalStateException("Overlapping slots detected");
                }

                boolean slotExists = availabilityRepo.existsByBarberIdAndStartTimeAndEndTime(
                barber.getId(), slotStart, slotEnd);
                
                if(slotExists){
                    throw new IllegalAccessError("Creneau existe");
                }

                Schedule schedule = scheduleRepo.findById(scheduleId)
                .orElseThrow(()-> new ResourceNotFoundException("No schedule found"));

                
                Availability slot = Availability.builder()
                .barber(barber)
                .startTime(slotStart)
                .endTime(slotEnd)
                .isAvailable(true)
                .note(" ")
                .schedule(schedule)
                .build();

                batchRequest.add(slot);
                batch++;
                
                //each time the max Batch request is attain, we save and then clear 
                if(batch >= maxBatchRequest){
                    slots.addAll(availabilityRepo.saveAll(batchRequest));
                    batchRequest.clear();
                    batch = 0;
                }
                currentSlotStart = currentSlotStart.plusMinutes(30);

            }

            if(!batchRequest.isEmpty()){
                slots.addAll(availabilityRepo.saveAll(batchRequest));                
            }
         
           return slots;
        
        }catch(Exception e ){
            throw new RuntimeException("Failed to generate time slots startTime: " + currentSlotStart.toString() + " endTime " + endTime.toString(), e);
        }
    }

    //Validate the hairsalon working hours
     private void validateWorkingHours(LocalDateTime startTime, LocalDateTime endTime, Barber barber) {
  
        if (startTime == null || endTime  == null || barber == null) {
            throw new IllegalArgumentException("Start time, end time, and barber cannot be null");
        }
    
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
    
        if (startTime.getHour() < 8 || endTime.getHour() > 20) {
            throw new IllegalArgumentException("Slots must be within business hours (8h-20h-)");
        }
    
        // Validate minimum slot duration (30 minutes)
        if (Duration.between(startTime, endTime).toMinutes() < 30) {
            throw new IllegalArgumentException("Time range must be at least 30 minutes");
        }


}

}
