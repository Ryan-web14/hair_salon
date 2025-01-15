package com.sni.hairsalon.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.sni.hairsalon.dto.response.AppointmentResponseDTO;

public interface EmailService {

    public CompletableFuture<Boolean> sendAppointmentConfirmation(String to, AppointmentResponseDTO appointment );
    /*public CompletableFuture<Boolean> sendAppointmentCancellationToClient(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendDailyScheduleToBarber(String barberEmail, List<AppointmentResponseDTO> appointments);
    public CompletableFuture<Boolean> sendAppointmentCancellationToBarber(String barberEamil);
    public CompletableFuture<Boolean> sendFirstReminder(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendFSecondReminder(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendCancellationNotification(String to, String barberEmail);*/
    //public void sendCheckInNotification(String barberEmail, AppointmentResponseDTO appointment);

}
