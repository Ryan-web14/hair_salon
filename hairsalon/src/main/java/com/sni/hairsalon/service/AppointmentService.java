package com.sni.hairsalon.service;


import java.time.LocalDate;
import java.util.List;

import com.sni.hairsalon.dto.request.AppointmentRequestDTO;
import com.sni.hairsalon.dto.request.AppointmentUpdateRequestDTO;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
public interface AppointmentService {
    
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request);
    public AppointmentResponseDTO updateAppointmentByAdmin(long id,AppointmentUpdateRequestDTO request);
    public AppointmentResponseDTO updateSAppointmentStatus(long id,int status);
    public void cancelAppointmentByClient(long id, String clientEmail);
    public void cancelAppointment(long id);
    public List<AppointmentResponseDTO> getAllAppointment();
    public List<AppointmentResponseDTO> getAllBarberAppointment(LocalDate date);
    public List<AppointmentResponseDTO> getBarberAppointment(long barberId);
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
    public void monitorAppointmentTime();
    public void monitorCheckInAppointment();
    public void InprogressToCompleted();
    public void deleteAllAppointment();
        
}
