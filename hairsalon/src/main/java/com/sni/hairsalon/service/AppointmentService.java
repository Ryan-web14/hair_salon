package com.sni.hairsalon.service;


import com.sni.hairsalon.dto.request.AppointmentRequestDTO;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
public interface AppointmentService {
    
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request);
    /*public AppointmentResponseDTO updateSAppointmentStatus(Status status);
    public void cancelAppointment(long id);
    public void clientCancelAppointment(long clientId,long id);
    public List<AppointmentResponseDTO> getAllBarberAppointment();
    public List<AppointmentResponseDTO> getBarberAppointment(long barberId);
    public List<AppointmentResponseDTO> getClientAppointment(long clientId);*/
        
}
