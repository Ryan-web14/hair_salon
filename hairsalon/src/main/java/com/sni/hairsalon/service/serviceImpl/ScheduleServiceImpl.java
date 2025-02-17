package com.sni.hairsalon.service.serviceImpl;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.AvailabilityRequestDTO;
import com.sni.hairsalon.dto.request.ScheduleRequestDTO;
import com.sni.hairsalon.dto.request.ScheduleTemplateRequestDTO;
import com.sni.hairsalon.dto.response.ScheduleResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.ScheduleMapper;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.Schedule;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.repository.ScheduleRepository;
import com.sni.hairsalon.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    
    private final ScheduleRepository scheduleRepo;
    private final BarberRepository barberRepo;
    private final ScheduleMapper mapper;
    private final AvailabilityServiceImpl availabilityService;

    @Override
    public ScheduleResponseDTO createSchedule(ScheduleRequestDTO request){
        Barber barber = barberRepo.findById(Long.parseLong(request.getBarberId()))
        .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));

    validateTimeRange(request.getStartTime(), request.getEndTime());

    if (hasAnyOverlappingSchedule(request)) {
        throw new IllegalStateException("Schedule overlap with another");
    }


    LocalDate currentDate = request.getEffectiveFrom();
    while (!currentDate.isAfter(request.getEffectiveTo())) {
        if (currentDate.getDayOfWeek().getValue() == request.getDayOfWeek()) {
            AvailabilityRequestDTO dto = AvailabilityRequestDTO.builder()
                .barberId(request.getBarberId())
                .starTime(LocalDateTime.of(currentDate, request.getStartTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES)))
                .endTime(LocalDateTime.of(currentDate, request.getEndTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES)))
                .isAvailable(true)
                .build();

            availabilityService.createAvailability(dto);
        }
        currentDate = currentDate.plusDays(1);
    }

    Schedule schedule = Schedule.builder()
        .barber(barber)
        .dayOfWeek(request.getDayOfWeek())
        .startTime(request.getStartTime())
        .endTime(request.getEndTime())
        .is_recurring(request.isRecurring())
        .effectiveFrom(Date.valueOf(request.getEffectiveFrom()))
        .effectiveTo(Date.valueOf(request.getEffectiveTo()))
        .build();

    return mapper.toDto(scheduleRepo.save(schedule));
}

    @Override
    public List<ScheduleResponseDTO> getBarberSchedule(long barberId){
        
        Barber barber = barberRepo.findById(barberId)
        .orElseThrow(()-> new ResourceNotFoundException("Barber not found"));

        List<Schedule> schedules = scheduleRepo.findByBarberId(barber.getId());

        if(schedules.isEmpty()){
            String.format("No schedule found for barber %d", barber.getId());
        }

        return schedules
        .stream()
        .map(schedule->mapper.toDto(schedule))
        .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponseDTO updateSchedule(long id, ScheduleRequestDTO request){
        
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
    public List<ScheduleResponseDTO> getAllCurrentSchedule(LocalDate date){
        
        List<Schedule> schedules = scheduleRepo.findCurrentSchedules(date);
        return schedules
        .stream()
        .map(schedule->mapper.toDto(schedule))
        .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponseDTO> createTemplateSchedule(ScheduleTemplateRequestDTO request){

        Barber barber = barberRepo.findById(Long.parseLong(request.getBarberId()))
        .orElseThrow(()-> new ResourceNotFoundException("Barber not found"));

        LocalTime startTime = request.getStartTime();
        LocalTime endTime = request.getEndTime();
        List<Schedule> schedules = new ArrayList<>();

        for(Integer dayOfWeek : request.getWorkingDays()){
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
        .map(schedule->mapper.toDto(schedule))
        .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponseDTO getBarberScheduleForDate(Long barberId, LocalDate date){

        Barber barber = barberRepo.findById(barberId)
        .orElseThrow(()-> new ResourceNotFoundException("Barber not found"));

        /*List<Schedule> specificSchedule = scheduleRepo.findNonRecurringSchedules(barber.getId(), date);

        if(!specificSchedule.isEmpty()){
            return specificSchedule
            .stream()
            .map(schedule->mapper.toDto(schedule))
            .collect(Collectors.toList());
        }
*/
        return mapper.toDto(
            scheduleRepo.findRecurringSchedules(barber.getId(), date.getDayOfWeek().getValue(), 
            date));
    }

    @Override
    public List<ScheduleResponseDTO> getBarBerTodayCurrentSchedule(long barberId){
        
        Barber barber = barberRepo.findById(barberId)
        .orElseThrow(()-> new ResourceNotFoundException("Barber not found"));

        List<Schedule> barberSchedule = scheduleRepo.findCurrentSchedulesForBaber(barber.getId(), LocalDate.now());

        return barberSchedule.stream()
        .map(schedule->mapper.toDto(schedule))
        .collect(Collectors.toList());

    }


    private void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime){
        
        if(startTime.isAfter(endTime)){
            throw new IllegalArgumentException("Start time must be before end time");
        }
        return;
    }

    private boolean hasAnyOverlappingSchedule(ScheduleRequestDTO request){
       
        Date effectiveFrom =  Date.valueOf(request.getEffectiveFrom());
       Date effectiveTo =  Date.valueOf(request.getEffectiveTo());
       
       
        return scheduleRepo.findOverlappingSchedules(
            Long.parseLong(request.getBarberId()),
            request.getDayOfWeek(),
            request.getStartTime(),
            request.getEndTime(),
            effectiveFrom,
            effectiveTo
            ).size() > 0;
    }

}

/*
    ;
    ;
    
    public List<ScheduleResponseDTO> createBulkSchedule(BulkScheduleRequestDTO request);
    public List<ScheduleResponseDTO> resolveConflicts(Long barberId, LocalDate date);
    public List<ScheduleResponseDTO> getBarberScheduleForDate(Long barberId, LocalDate date);
    public List<ScheduleResponseDTO> getAllSchedule(); */