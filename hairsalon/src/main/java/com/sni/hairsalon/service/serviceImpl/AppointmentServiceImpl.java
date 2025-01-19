 package com.sni.hairsalon.service.serviceImpl;

import java.io.ObjectInputFilter.Status;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.AppointmentRequestDTO;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.dto.response.AvailabilityResponseDTO;
import com.sni.hairsalon.dto.response.ScheduleResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.AppointmentMapper;
import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.model.Availability;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.repository.AppointmentRepository;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.repository.ClientRepository;
import com.sni.hairsalon.repository.HaircutRepository;
import com.sni.hairsalon.service.AppointmentService;
import com.sni.hairsalon.service.EmailService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    
    private final ScheduleServiceImpl  scheduleService;
    private final AvailabilityServiceImpl availabilityService;
    private final EmailService mailSender;
    private final ClientRepository clientRepo;
    private final BarberRepository barberRepo;
    private final HaircutRepository haircutRepo;
    private final AppointmentRepository appointmentRepo;
    private final AppointmentMapper mapper;
    
   @Transactional
   @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request){
        
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

         if(!availabilityService.isAvailableSlot(barber.getId(),
         appointmentTime, haircut.getDuration())){

            throw new IllegalStateException("Slot is not available");
         }

         availabilityService.makeSlotUnavailable(barber.getId(),appointmentTime,haircut.getDuration());

      Appointment appointment = Appointment.builder()
      .client(client)
      .barber(barber)
      .haircut(haircut)
      .appointmentTime(appointmentTime)
      .status(com.sni.hairsalon.model.Status.CONFIRMED.getCode())
      .build();
      appointmentRepo.save(appointment);
      AppointmentResponseDTO response =  mapper.toDto(appointment);
      mailSender.sendAppointmentConfirmation(client.getUser().getEmail(),response);
      
      return response;
    }
 }
