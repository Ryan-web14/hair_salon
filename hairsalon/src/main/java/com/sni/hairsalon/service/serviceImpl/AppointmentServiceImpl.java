 package com.sni.hairsalon.service.serviceImpl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.request.AppointmentRequestDTO;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.dto.response.ScheduleResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.mapper.AppointmentMapper;
import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.repository.AppointmentRepository;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.repository.ClientRepository;
import com.sni.hairsalon.repository.HaircutRepository;
import com.sni.hairsalon.service.AppointmentService;
import com.sni.hairsalon.service.EmailService;
import com.sni.hairsalon.model.Status;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    
    private final ScheduleServiceImpl  scheduleService;
    private final AvailabilityServiceImpl availabilityService;
    private final EmailService mailService;
    private final BarberRepository barberRepo;
    private final HaircutRepository haircutRepo;
    private final AppointmentRepository appointmentRepo;
    private final AppointmentMapper mapper;
   
    @Autowired
    private ClientRepository clientRepo;
    
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
      .status(Status.CONFIRMED.getCode())
      .build();
      appointmentRepo.save(appointment);
      AppointmentResponseDTO response =  mapper.toDto(appointment);
      mailService.sendAppointmentConfirmation(client.getUser().getEmail(),response);
      
      return response;
    }



    @Scheduled(fixedRate = 30000)
    public void monitorAppointmentTime(){
      LocalDateTime now = LocalDateTime.now();
      List<Appointment> appointments = appointmentRepo.findByAppointmentTimeBetweenAndStatus
      (now.with(LocalTime.MIDNIGHT).truncatedTo(ChronoUnit.MINUTES),
       now.with(LocalTime.MAX).truncatedTo(ChronoUnit.MINUTES),
       Status.CONFIRMED.getCode()
       );
       
       for(Appointment appointment : appointments){
         checkAppointmentTime(appointment, now);
       }
    }


    private void checkAppointmentTime(Appointment appointment, LocalDateTime now){
      LocalDateTime appointmentTime = appointment.getAppointmentTime();
      Duration difference = Duration.between(appointmentTime, now);
      long late =  difference.toMinutes();

      if(late >= 5 && late < 10){
         mailService.sendFirstReminder(appointment.getClient()
         .getUser().getEmail(), mapper.toDto(appointment));
      }
      else if(late >= 10 && late < 15){
         mailService.sendSecondReminder(appointment.getClient()
         .getUser().getEmail(), mapper.toDto(appointment));
      }
      else if(late >= 15){
         appointment.setStatus(Status.NO_SHOW.getCode());
         appointmentRepo.save(appointment);
         mailService.sendCancellationNotification(appointment.getClient()
         .getUser().getEmail(), mapper.toDto(appointment));
         mailService.sendAppointmentCancellationToBarber(appointment.getBarber()
         .getUser().getEmail(), mapper.toDto(appointment));
      }
    }
 }

 
    /*   PENDING(1),
    REQUESTED(2),
    CONFIRMED(3),
    CHECK_IN(4),
    IN_PROGRESS(5),
    COMPLETED(5),
    CANCELLED_BY_CLIENT(6),
    CANCELLED_BY_PROVIDER(7),
    RESCHEDULED(8),
    NO_SHOW( */