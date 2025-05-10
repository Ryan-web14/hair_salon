package com.sni.hairsalon.service;

import com.infobip.ApiException;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;

public interface WhatsappService {
    public void sendAppointmentConfirmation(AppointmentResponseDTO appointment) throws ApiException;
    public void sendAppointmentConfirmationToProvider(AppointmentResponseDTO appointment) throws ApiException;
    public void sendCheckinToProvider(AppointmentResponseDTO appointment)throws ApiException;

}