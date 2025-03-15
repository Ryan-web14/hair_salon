package com.sni.hairsalon.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.AppointmentRequestDTO;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.model.Status;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.repository.ClientRepository;
import com.sni.hairsalon.repository.HaircutRepository;
import com.sni.hairsalon.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {
    
    private final UserRepository userRepo;
    private final ClientRepository clientRepo;
    private final BarberRepository barberRepo;
    private final HaircutRepository haircutRepo;

    public Appointment toEntity(AppointmentRequestDTO request){
        User user =  userRepo.findUserByEmail(request.getEmail())
        .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        Client client = clientRepo.findClientByUserID(user.getId())
        .orElseThrow(()-> new ResourceNotFoundException("client not found"));
        Barber barber = barberRepo.findById(Long.parseLong(request.getBarberId()))
        .orElseThrow(()-> new ResourceNotFoundException("barber not found"));
        Haircut haircut = haircutRepo.findHaircutByType(request.getHaircutType())
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

    public AppointmentResponseDTO toDto(Appointment appointment){
        AppointmentResponseDTO dto = AppointmentResponseDTO.builder()
        .id(String.valueOf(appointment.getId()))
        .clientId(String.valueOf(appointment.getClient().getId()))
        .clientFirstname(appointment.getClient().getFirstname())
        .clientLastname(appointment.getClient().getLastname())
        .clientEmail(appointment.getClient().getUser().getEmail())
        .barberId(String.valueOf(appointment.getBarber().getId()))
        .barberFirstname(appointment.getBarber().getFirstname())
        .barberLastname(appointment.getBarber().getLastname())
        .barberEmail(appointment.getBarber().getUser().getEmail())
        .appointmentTime(appointment.getAppointmentTime())
        .haircutType(appointment.getHaircut().getType())
        .price(appointment.getHaircut().getPrice())
        .status(Status.toEnum(appointment.getStatus()).name())
        .build();
        return dto;
    }
    
}
