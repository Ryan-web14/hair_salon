package com.sni.hairsalon.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.model.Barber;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.model.Haircut;
import com.sni.hairsalon.model.Status;
import com.sni.hairsalon.model.User;
import com.sni.hairsalon.repository.AppointmentRepository;
import com.sni.hairsalon.repository.ClientRepository;
import com.sni.hairsalon.service.EmailService;
import com.sni.hairsalon.service.serviceImpl.AppointmentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private ClientRepository clientRepo;
    
    @Mock
    private AppointmentRepository appointmentRepo;
    
    @Mock
    private EmailService mailService;
    
    @InjectMocks
    private AppointmentServiceImpl appointmentService;
    
    @Test
    public void testCheckIn_Success() {
        // Arrange
        String email = "client@example.com";
        LocalDateTime now = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.MINUTES);
        LocalDate currentDate = now.toLocalDate();
        
        Client client = new Client();
        client.setId(1L);
        
        User user = new User();
        user.setEmail(email);
        client.setUser(user);
        
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setClient(client);
        appointment.setAppointmentTime(now.plusMinutes(10)); // Rendez-vous dans 10 minutes
        appointment.setStatus(Status.CONFIRMED.getCode());
        
        Barber barber = new Barber();
        barber.setId(1L);
        User barberUser = new User();
        barberUser.setEmail("barber@example.com");
        barber.setUser(barberUser);
        appointment.setBarber(barber);
        
        Haircut haircut = new Haircut();
        haircut.setType("Standard");
        haircut.setDuration(30);
        appointment.setHaircut(haircut);
        
        List<Appointment> appointments = Collections.singletonList(appointment);
        
        when(clientRepo.findByEmail(email)).thenReturn(Optional.of(client));
        when(appointmentRepo.findByClientAndDateAndStatus(
            eq(client.getId()), 
            eq(currentDate), 
            eq(Status.CONFIRMED.getCode())
        )).thenReturn(appointments);
        
        doReturn(CompletableFuture.completedFuture(true))
            .when(mailService).sendCheckIn(any(Appointment.class));
        
        // Act
        AppointmentResponseDTO result = appointmentService.checkIn(email);
        
        // Assert
        assertNotNull(result);
        assertEquals(String.valueOf(appointment.getId()), result.getId());
        assertEquals(Status.CHECK_IN.name(), result.getStatus());
        
        verify(appointmentRepo).save(any(Appointment.class));
        verify(mailService).sendCheckIn(any(Appointment.class));
    }
    
    @Test
    public void testCheckIn_ClientNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(clientRepo.findByEmail(email)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.checkIn(email);
        });
        
        verify(appointmentRepo, never()).save(any(Appointment.class));
        verify(mailService, never()).sendCheckIn(any(Appointment.class));
    }
    
    @Test
    public void testCheckIn_NoAppointmentsForToday() {
        // Arrange
        String email = "client@example.com";
        LocalDate currentDate = LocalDate.now();
        
        Client client = new Client();
        client.setId(1L);
        
        User user = new User();
        user.setEmail(email);
        client.setUser(user);
        
        when(clientRepo.findByEmail(email)).thenReturn(Optional.of(client));
        when(appointmentRepo.findByClientAndDateAndStatus(
            eq(client.getId()), 
            eq(currentDate), 
            eq(Status.CONFIRMED.getCode())
        )).thenReturn(Collections.emptyList());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            appointmentService.checkIn(email);
        });
        
        verify(appointmentRepo, never()).save(any(Appointment.class));
        verify(mailService, never()).sendCheckIn(any(Appointment.class));
    }
    
    @Test
    public void testCheckIn_TooEarly() {
        // Arrange
        String email = "client@example.com";
        LocalDateTime now = LocalDateTime.now().plusHours(1);
        LocalDate currentDate = now.toLocalDate();
        
        Client client = new Client();
        client.setId(1L);
        
        User user = new User();
        user.setEmail(email);
        client.setUser(user);
        
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setClient(client);
        appointment.setAppointmentTime(now.plusMinutes(20)); // Rendez-vous dans 20 minutes
        appointment.setStatus(Status.CONFIRMED.getCode());
        
        List<Appointment> appointments = Collections.singletonList(appointment);
        
        when(clientRepo.findByEmail(email)).thenReturn(Optional.of(client));
        when(appointmentRepo.findByClientAndDateAndStatus(
            eq(client.getId()), 
            eq(currentDate), 
            eq(Status.CONFIRMED.getCode())
        )).thenReturn(appointments);
        
        // Act & Assert
        // On s'attend à une exception car le client est arrivé trop tôt 
        // (plus de 15 minutes avant le rendez-vous)
        assertThrows(IllegalStateException.class, () -> {
            appointmentService.checkIn(email);
        });
        
        verify(appointmentRepo, never()).save(any(Appointment.class));
        verify(mailService, never()).sendCheckIn(any(Appointment.class));
    }
    
    @Test
    public void testCheckIn_TooLate() {
        // Arrange
        String email = "client@example.com";
        LocalDateTime now = LocalDateTime.now().plusHours(1);
        LocalDate currentDate = now.toLocalDate();
        
        Client client = new Client();
        client.setId(1L);
        
        User user = new User();
        user.setEmail(email);
        client.setUser(user);
        
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setClient(client);
        appointment.setAppointmentTime(now.minusMinutes(20)); // Rendez-vous était il y a 20 minutes
        appointment.setStatus(Status.CONFIRMED.getCode());
        
        List<Appointment> appointments = Collections.singletonList(appointment);
        
        when(clientRepo.findByEmail(email)).thenReturn(Optional.of(client));
        when(appointmentRepo.findByClientAndDateAndStatus(
            eq(client.getId()), 
            eq(currentDate), 
            eq(Status.CONFIRMED.getCode())
        )).thenReturn(appointments);
        
        // Act & Assert
        // On s'attend à une exception car le client est arrivé trop tard
        // (plus de 15 minutes après le rendez-vous)
        assertThrows(IllegalStateException.class, () -> {
            appointmentService.checkIn(email);
        });
        
        verify(appointmentRepo, never()).save(any(Appointment.class));
        verify(mailService, never()).sendCheckIn(any(Appointment.class));
    }
}