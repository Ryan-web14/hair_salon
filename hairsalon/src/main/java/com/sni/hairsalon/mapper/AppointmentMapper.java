package com.sni.hairsalon.mapper;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.AppointmentRequestDTO;
import com.sni.hairsalon.dto.response.AppointmentResponseDto;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.model.Status;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.repository.ClientRepository;
import com.sni.hairsalon.repository.HaircutRepository;

@Component
public class AppointmentMapper {

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private BarberRepository barberRepo;

    @Autowired
    private HaircutRepository haircutRepo;

    public Appointment toEntity(AppointmentRequestDTO request){
        Client client = clientRepo.findById(request.getIdClient())
        .orElseThrow(()-> new ResourceNotFoundException("client not found"));
        Barber barber = barberRepo.findById(request.getIdBarber())
        .orElseThrow(()-> new ResourceNotFoundException("barber not found"));
        Haircut haircut = haircutRepo.findHaircutById(request.getIdHaircut())
        .orElseThrow(()-> new ResourceNotFoundException("haircut not found")); 
        Appointment appointment = Appointment.builder()
        .client(client)
        .barber(barber)
        .haircut(haircut)
        .appointmentTime(request.getAppointmentTime())
        .bookedTime(LocalDateTime.now())
        .status(Status.PENDING.getCode())
        .build();
        return appointment;
    }

    public AppointmentResponseDto toDto(Appointment appointment){
        AppointmentResponseDto dto = AppointmentResponseDto.builder()
        .id(appointment.getId())
        .clientId(appointment.getClient().getId())
        .clientFirstname(appointment.getClient().getFirstname())
        .clientFirstname(appointment.getClient().getLastname())
        .barberId(appointment.getBarber().getId())
        .barberFirstname(appointment.getBarber().getFirstname())
        .barberFirstname(appointment.getBarber().getLastname())
        .appointmentTime(appointment.getAppointmentTime())
        .bookedTime(appointment.getBookedTime())
        .haircutType(appointment.getHaircut().getType())
        .price(appointment.getHaircut().getPrice())
        .status(Status.toEnum(appointment.getStatus()).name())
        .build();
        return dto;
    }
    
}
