package com.sni.hairsalon.service;


import com.sni.hairsalon.model.UserPrincipal;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

import com.sni.hairsalon.dto.request.AppointmentRequestDTO;
import com.sni.hairsalon.dto.request.AppointmentUpdateRequestDTO;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
public interface AppointmentService {
    
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request);
    public AppointmentResponseDTO updateAppointmentByAdmin(long id,AppointmentUpdateRequestDTO request);
    public AppointmentResponseDTO updateSAppointmentStatus(long id,int status);
    public void cancelAppointmentByClient(long id, String clientEmail);
    public void cancelAppointment(long id);
    public List<AppointmentResponseDTO> getAllAppointment();
    public List<AppointmentResponseDTO> getAllEstheticianAppointments();
    public List<AppointmentResponseDTO> getAllBarberAppointments();
    public List<AppointmentResponseDTO> getAllBarberAppointment(LocalDate date);
    public List<AppointmentResponseDTO> getAllEstheticianAppointment(LocalDate date);
    public List<AppointmentResponseDTO> getAllEstheticianCompletedAppointments();
    public List<AppointmentResponseDTO> getBarberAppointment(long barberId);
    public List<AppointmentResponseDTO> getAllBarberCompletedAppointments();
    public List<AppointmentResponseDTO> getEstheticianAppointment(long estheticianId);
    public List<AppointmentResponseDTO> getMyBarberAppointment(String email);
    public List<AppointmentResponseDTO> getClientAppointment(long clientId);
    public List<AppointmentResponseDTO> getCompletedAppointment();
    public List<AppointmentResponseDTO> getClientCompletedAppointment(String email);
    public void makeAppointmentCompleted(long id);
    public List<AppointmentResponseDTO> searchAppointmentByClientName(String name, int status);
    public AppointmentResponseDTO checkIn(String email);
    public int clientCount();
    public int countAppointmentForTheDay();
    public void sendDailyAppointmentScheduleToBarber(); 
    public void sendDailyAppointmentScheduleToEsthetician();
    public void monitorAppointmentTime();
    public void monitorCheckInAppointment();
    public void InprogressToCompleted();
    public void deleteAllAppointment();
    public void remindAppointment();
    public List<AppointmentResponseDTO> getProviderSpecificAppointment(UserPrincipal authenticatedUser, LocalDate date) throws AccessDeniedException, ResourceNotFoundException;        
}
