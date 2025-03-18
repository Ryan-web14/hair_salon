package com.sni.hairsalon.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.sni.hairsalon.dto.request.AppointmentRequestDTO;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.Esthetic;
import com.sni.hairsalon.model.Esthetician;
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.model.Status;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.repository.BarberRepository;
import com.sni.hairsalon.repository.ClientRepository;
import com.sni.hairsalon.repository.EstheticRepository;
import com.sni.hairsalon.repository.EstheticianRepository;
import com.sni.hairsalon.repository.HaircutRepository;
import com.sni.hairsalon.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {
    
    private final UserRepository userRepo;
    private final ClientRepository clientRepo;
    private final BarberRepository barberRepo;
    private final EstheticianRepository estheticianRepo;
    private final EstheticRepository estheticRepo;
    private final HaircutRepository haircutRepo;

  public Appointment toEntity(AppointmentRequestDTO request){
    User user = userRepo.findUserByEmail(request.getEmail())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    
    Client client = clientRepo.findClientByUserID(user.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    
    Appointment.AppointmentBuilder appointmentBuilder = Appointment.builder()
        .client(client)
        .appointmentTime(request.getAppointmentTime())
        .bookedTime(LocalDateTime.now())
        .status(Status.PENDING.getCode());
    
        //with barber
    if (request.getBarberId() != null && !request.getBarberId().isEmpty()) {
        Barber barber = barberRepo.findById(Long.parseLong(request.getBarberId()))
            .orElseThrow(() -> new ResourceNotFoundException("Barber not found"));
        
        Haircut haircut = haircutRepo.findHaircutByType(request.getHaircutType())
            .orElseThrow(() -> new ResourceNotFoundException("Haircut not found"));
        
        appointmentBuilder.barber(barber).haircut(haircut);
    } 
    //with esthetician
    else if (request.getEstheticianId() != null && !request.getEstheticianId().isEmpty()) {
        Esthetician esthetician = estheticianRepo.findById(Long.parseLong(request.getEstheticianId()))
            .orElseThrow(() -> new ResourceNotFoundException("Esthetician not found"));
        
        Esthetic esthetic = estheticRepo.findEstheticByType(request.getEstheticType())
            .orElseThrow(() -> new ResourceNotFoundException("Esthetic service not found"));
        
        appointmentBuilder.esthetician(esthetician).esthetic(esthetic);
    } 
    else {
        throw new IllegalArgumentException("Either barberId or estheticianId must be provided");
    }
    
    return appointmentBuilder.build();
}

    public AppointmentResponseDTO toDto(Appointment appointment) {
        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        
        // Set basic appointment details
        dto.setId(String.valueOf(appointment.getId()));
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setBookedTime(appointment.getBookedTime());
        dto.setStatus(Status.toEnum(appointment.getStatus()).name());
        
        // Set client details
        if (appointment.getClient() != null) {
            dto.setClientId(String.valueOf(appointment.getClient().getId()));
            dto.setClientFirstname(appointment.getClient().getFirstname());
            dto.setClientLastname(appointment.getClient().getLastname());
            dto.setClientEmail(appointment.getClient().getUser().getEmail());
        }
        
        // Set barber details if present
        if (appointment.getBarber() != null) {
            dto.setBarberId(String.valueOf(appointment.getBarber().getId()));
            dto.setBarberFirstname(appointment.getBarber().getFirstname());
            dto.setBarberLastname(appointment.getBarber().getLastname());
            
            if (appointment.getBarber().getUser() != null) {
                dto.setBarberEmail(appointment.getBarber().getUser().getEmail());
            }
            
            // Set haircut details if present
            if (appointment.getHaircut() != null) {
                dto.setHaircutType(appointment.getHaircut().getType());
                dto.setPrice(appointment.getHaircut().getPrice());
            }
        }
        
        // Set esthetician details if present
        if (appointment.getEsthetician() != null) {
            dto.setEstheticianId(String.valueOf(appointment.getEsthetician().getId()));
            dto.setEstheticianFirstname(appointment.getEsthetician().getFirstname());
            dto.setEstheticianLastname(appointment.getEsthetician().getLastname());
            
            if (appointment.getEsthetician().getUser() != null) {
                dto.setEstheticianEmail(appointment.getEsthetician().getUser().getEmail());
            }
            
            // Set esthetic service details if present
            if (appointment.getEsthetic() != null) {
                dto.setEstheticType(appointment.getEsthetic().getType());
                dto.setPrice(appointment.getEsthetic().getPrice());
            }
        }
        
        return dto;
    }
}
