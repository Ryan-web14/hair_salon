 package com.sni.hairsalon.service.serviceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.AppointmentRequestDTO;
import com.sni.hairsalon.dto.response.ScheduleResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.repository.ClientRepository;
import com.sni.hairsalon.repository.HaircutRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    
    private final ScheduleServiceImpl  scheduleService;
    private final AvailabilityServiceImpl availabilityService;
    private final ClientRepository clientRepo;
    private final BarberRepository barberRepo;
    private final HaircutRepository haircutRepo;

   @Override
    public AppointmentResponse createAppointment(AppointmentRequestDTO request){
        
        Client client = clientRepo.findById(request.getClientId())
        .orElseThrow(()-> new ResourceNotFoundException("Client not found"));

        Barber barber = barberRepo.findById(request.getBarberId())
        .orElseThrow(()-> new ResourceNotFoundException("Barber not found"));

        Haircut haircut = haircutRepo.findById(request.getHaircutId())
        .orElseThrow(()-> new ResourceNotFoundException("Haircut not found"));

        LocalDate appointmentDate = request.getAppointmentTime().toLocalDate();
        LocalDateTime appointmentTime = request.getAppointmentTime();

        ScheduleResponseDTO schedule = scheduleService.getBarberScheduleForDate(request.getBarberId(),
         appointmentDate); 
        
        LocalDateTime startSchedule = schedule.getStartTime();
        LocalDateTime endSchedule = schedule.getEndTime();

        if(appointmentDate.isBefore(schedule.getEffectiveFrom()) ||
         appointmentDate.isAfter(schedule.getEffectiveTo())){
            throw new IllegalStateException("The appointment date doesn't match the barber schedule");
         }

         if(appointmentTime.isBefore(startSchedule) 
         ||appointmentTime.isAfter(endSchedule)){
            throw new IllegalStateException("The appointment time doesn't match the barber hour");
         }

         
         
         



    }
 }
