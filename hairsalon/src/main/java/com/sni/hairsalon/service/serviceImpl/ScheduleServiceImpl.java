package com.sni.hairsalon.service.serviceImpl;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.AvailabilityRequestDTO;
import com.sni.hairsalon.dto.request.BulkScheduleRequestDTO;
import com.sni.hairsalon.dto.request.ScheduleRequestDTO;
import com.sni.hairsalon.dto.request.ScheduleTemplateRequestDTO;
import com.sni.hairsalon.dto.response.ScheduleResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.ScheduleMapper;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.Esthetician;
import com.sni.hairsalon.model.Schedule;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.repository.EstheticianRepository;
import com.sni.hairsalon.repository.ScheduleRepository;
import com.sni.hairsalon.service.AvailabilityService;
import com.sni.hairsalon.service.ScheduleService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepo;
    private final BarberRepository barberRepo;
    private final EstheticianRepository estheticianRepo; 
    private final EstheticianRepository esthecianRepo;
    private final ScheduleMapper mapper;
    private final AvailabilityService availabilityService;

    @Override
    @Transactional
    public ScheduleResponseDTO createSchedule(ScheduleRequestDTO request) {
        Barber barber = barberRepo.findById(Long.parseLong(request.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));

        validateTimeRange(request.getStartTime(), request.getEndTime());

        if (hasAnyOverlappingSchedule(request)) {
            throw new IllegalStateException("Schedule overlap with another");
        }

        Schedule schedule = Schedule.builder()
                .barber(barber)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .is_recurring(true)
                .effectiveFrom(Date.valueOf(request.getEffectiveFrom()))
                .effectiveTo(Date.valueOf(request.getEffectiveTo()))
                .build();

        LocalDate currentDate = request.getEffectiveFrom();
        int batchSize = 0;
        int maxBatchSize = 5;
        List<AvailabilityRequestDTO> batchRequest = new ArrayList<>();

        while (!currentDate.isAfter(request.getEffectiveTo())) {
            if (currentDate.getDayOfWeek().getValue() == request.getDayOfWeek()) {
                AvailabilityRequestDTO dto = AvailabilityRequestDTO.builder()
                        .barberId(request.getId())
                        .starTime(LocalDateTime.of(currentDate,
                                request.getStartTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES)))
                        .endTime(LocalDateTime.of(currentDate,
                                request.getEndTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES)))
                        .isAvailable(true)
                        .build();

                batchRequest.add(dto);

                batchSize++;

                if (batchSize >= maxBatchSize) {
                    batchSize = 0;
                    processBatch(batchRequest);
                    batchRequest.clear();
                }

            }

            currentDate = currentDate.plusDays(1);
        }

        if (!batchRequest.isEmpty()) {
            processBatch(batchRequest);
        }

        return mapper.toDto(scheduleRepo.save(schedule));
    }

    @Override
@Transactional
public List<ScheduleResponseDTO> bulkCreateSchedules(BulkScheduleRequestDTO request) {
    // Determine and load the appropriate service provider
    String providerType;
    Object provider;
    
    if (request.getBarberId() != null && !request.getBarberId().isEmpty()) {
        providerType = "barber";
        provider = barberRepo.findById(Long.parseLong(request.getBarberId()))
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));
    } else if (request.getEstheticianId() != null && !request.getEstheticianId().isEmpty()) {
        providerType = "esthetician";
        provider = estheticianRepo.findById(Long.parseLong(request.getEstheticianId()))
                .orElseThrow(() -> new ResourceNotFoundException("Esthetician not found"));
    } else {
        throw new IllegalArgumentException("Either barberId or estheticianId must be provided");
    }

    validateTimeRange(request.getStartTime(), request.getEndTime());

    // Check for overlapping schedules
    for (Integer day : request.getWorkingDays()) {
        ScheduleRequestDTO dto = ScheduleRequestDTO.builder()
                .id(providerType.equals("barber") ? request.getBarberId() : request.getEstheticianId())
                .dayOfWeek(day)
                .effectiveFrom(request.getEffectiveFrom())
                .effectiveTo(request.getEffectiveTo())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .isRecurring(true)
                .build();

        if (hasAnyOverlappingSchedule(dto)) {
            throw new IllegalStateException("Schedule overlap with another");
        }
    }

    List<ScheduleResponseDTO> schedules = new ArrayList<>();

    // Managing the creation of availabilities through batches
    LocalDate currentDate = request.getEffectiveFrom();
    int batch = 0;
    int maxBatch = 10;
    List<AvailabilityRequestDTO> batchRequest = new ArrayList<>();

    while (currentDate.isBefore(request.getEffectiveTo())) {
        if (request.getWorkingDays().contains(currentDate.getDayOfWeek().getValue())) {
            Schedule schedule = Schedule.builder()
                    .dayOfWeek(currentDate.getDayOfWeek().getValue())
                    .startTime(request.getStartTime())
                    .endTime(request.getEndTime())
                    .is_recurring(true)
                    .effectiveFrom(Date.valueOf(request.getEffectiveFrom()))
                    .effectiveTo(Date.valueOf(request.getEffectiveTo()))
                    .build();
            
            // Set the appropriate provider
            if (providerType.equals("barber")) {
                schedule.setBarber((Barber) provider);
            } else {
                schedule.setEsthetician((Esthetician) provider);
            }

            scheduleRepo.save(schedule);
            schedules.add(mapper.toDto(schedule));

            AvailabilityRequestDTO dto = AvailabilityRequestDTO.builder()
                    .starTime(LocalDateTime.of(currentDate,
                            request.getStartTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES)))
                    .endTime(LocalDateTime.of(currentDate,
                            request.getEndTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES)))
                    .isAvailable(true)
                    .scheduleId(schedule.getId())
                    .build();
            
            // Set the appropriate provider ID
            if (providerType.equals("barber")) {
                dto.setBarberId(request.getBarberId());
            } else {
                dto.setEstheticianId(request.getEstheticianId());
            }
            
            batchRequest.add(dto);
            batch++;

            if (batch >= maxBatch) {
                batch = 0;
                processBatch(batchRequest);
                batchRequest.clear();
            }
        }
        currentDate = currentDate.plusDays(1);
    }

    if (!batchRequest.isEmpty()) {
        processBatch(batchRequest);
    }

    return schedules;
}

    @Override
    public List<ScheduleResponseDTO> getBarberSchedule(long barberId) {

        Barber barber = barberRepo.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));

        List<Schedule> schedules = scheduleRepo.findByBarberId(barber.getId());

        if (schedules.isEmpty()) {
            String.format("No schedule found for barber %d", barber.getId());
        }

        Set<String> uniqueDay = new HashSet<>();
        List<ScheduleResponseDTO> uniqueSchedule = new ArrayList<>();

        for(Schedule schedule : schedules){
            DayOfWeek dayOfWeek = DayOfWeek.of(schedule.getDayOfWeek());
            String day = dayOfWeek.toString();

            if(!uniqueDay.contains(day)){
                uniqueDay.add(day);
                uniqueSchedule.add(mapper.toDto(schedule));
            }
        }

        return uniqueSchedule;
    }

    @Override
    public List<ScheduleResponseDTO> getEstheticianSchedule(long estheticianId) {
    Esthetician esthetician = estheticianRepo.findById(estheticianId)
            .orElseThrow(() -> new ResourceNotFoundException("Esthetician not found"));

    List<Schedule> schedules = scheduleRepo.findByEstheticianId(esthetician.getId());

    if (schedules.isEmpty()) {
        String.format("No schedule found for esthetician %d", esthetician.getId());
    }

    Set<String> uniqueDay = new HashSet<>();
    List<ScheduleResponseDTO> uniqueSchedule = new ArrayList<>();

    for(Schedule schedule : schedules) {
        DayOfWeek dayOfWeek = DayOfWeek.of(schedule.getDayOfWeek());
        String day = dayOfWeek.toString();

        if(!uniqueDay.contains(day)) {
            uniqueDay.add(day);
            uniqueSchedule.add(mapper.toDto(schedule));
        }
    }

    return uniqueSchedule;
}

    @Override
    public ScheduleResponseDTO updateSchedule(long id, ScheduleRequestDTO request) {

        Schedule schedule = scheduleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        validateTimeRange(request.getStartTime(), request.getEndTime());

        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.set_recurring(request.isRecurring());
        schedule.setEffectiveFrom(Date.valueOf(request.getEffectiveFrom()));
        schedule.setEffectiveTo(Date.valueOf(request.getEffectiveTo()));
        Schedule updatedSchedule = scheduleRepo.save(schedule);
        return mapper.toDto(updatedSchedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id){
        
        Schedule schedule = scheduleRepo.findById(id)
        .orElseThrow(()-> new ResourceNotFoundException("Schedule not found"));

        scheduleRepo.deleteById(schedule.getId());

        return;
    }

    @Override
    @Transactional
    public void deleteAllSchedule(){

        scheduleRepo.deleteAll();
        return;
    }

    @Override
    @Transactional
    public List<ScheduleResponseDTO> getAllCurrentSchedule(LocalDate date) {

        List<Schedule> schedules = scheduleRepo.findCurrentSchedules(date);
        return schedules
                .stream()
                .map(schedule -> mapper.toDto(schedule))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteScheduleByBarberId(Long barberId){

        scheduleRepo.deleteByBarberId(barberId);
        return;
    }

    @Override
    public List<ScheduleResponseDTO> createTemplateSchedule(ScheduleTemplateRequestDTO request) {

        Barber barber = barberRepo.findById(Long.parseLong(request.getBarberId()))
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));

        LocalTime startTime = request.getStartTime();
        LocalTime endTime = request.getEndTime();
        List<Schedule> schedules = new ArrayList<>();

        for (Integer dayOfWeek : request.getWorkingDays()) {
            Schedule schedule = Schedule.builder()
                    .barber(barber)
                    .dayOfWeek(dayOfWeek)
                    .startTime(LocalDateTime.of(request.getEffectiveFrom(), startTime))
                    .endTime(LocalDateTime.of(request.getEffectiveFrom(), endTime))
                    .is_recurring(true)
                    .effectiveFrom(Date.valueOf(request.getEffectiveFrom()))
                    .effectiveTo(Date.valueOf(request.getEffectiveTo()))
                    .build();

            schedules.add(schedule);
        }

        scheduleRepo.saveAll(schedules);

        return schedules.stream()
                .map(schedule -> mapper.toDto(schedule))
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponseDTO getBarberScheduleForDate(Long barberId, LocalDate date) {

        Barber barber = barberRepo.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));

        Schedule schedule = scheduleRepo
                .findCurrentSchedulesForBarber(barber.getId(), date, date.getDayOfWeek().getValue())
                .orElseThrow(() -> new ResourceNotFoundException("No found schedule"));
        return mapper.toDto(schedule);
    }

    @Override
    public ScheduleResponseDTO getEstheticianScheduleForDate(Long estheticianId, LocalDate date) {

        Esthetician esthetician = esthecianRepo.findById(estheticianId)
            .orElseThrow(() -> new ResourceNotFoundException("Esthetician not found"));

        Schedule schedule = scheduleRepo
                .findCurrentSchedulesForEsthetician(esthetician.getId(), date, date.getDayOfWeek().getValue())
                .orElseThrow(() -> new ResourceNotFoundException("No found schedule"));
        return mapper.toDto(schedule);
    }


    @Override
    public ScheduleResponseDTO getBarBerTodayCurrentSchedule(long barberId) {

        Barber barber = barberRepo.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));

        Schedule barberSchedule = scheduleRepo
                .findCurrentSchedulesForBarber(barber.getId(), LocalDate.now(),
                        LocalDate.now().getDayOfWeek().getValue())
                .orElseThrow(() -> new ResourceNotFoundException("No schedule found"));

        return mapper.toDto(barberSchedule);

    }

    private void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {

        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        return;
    }

    private boolean hasAnyOverlappingSchedule(ScheduleRequestDTO request) {

        Date effectiveFrom = Date.valueOf(request.getEffectiveFrom());
        Date effectiveTo = Date.valueOf(request.getEffectiveTo());

        return scheduleRepo.findOverlappingSchedules(
                Long.parseLong(request.getId()),
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime(),
                effectiveFrom,
                effectiveTo).size() > 0;
    }

    private void processBatch(List<AvailabilityRequestDTO> batch) {

        for (AvailabilityRequestDTO dto : batch) {
            availabilityService.createAvailability(dto);
        }
    }


}

/*
 * ;
 * ;
 * 
 * public List<ScheduleResponseDTO> createBulkSchedule(BulkScheduleRequestDTO
 * request);
 * public List<ScheduleResponseDTO> resolveConflicts(Long barberId, LocalDate
 * date);
 * 
 * public List<ScheduleResponseDTO> getAllSchedule();
 */