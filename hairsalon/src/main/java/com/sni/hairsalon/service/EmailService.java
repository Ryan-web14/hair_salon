package com.sni.hairsalon.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.sni.hairsalon.dto.response.AppointmentResponseDTO;

public interface EmailService {

    public CompletableFuture<Boolean> sendAppointmentConfirmation(String to, AppointmentResponseDTO appointment );
    //public void sendCheckInNotification(String barberEmail, AppointmentResponseDTO appointment);
    //public CompletableFuture<Boolean> sendDailyScheduleToBarber(String barberEmail, List<AppointmentResponseDTO> appointments);
    public CompletableFuture<Boolean> sendAppointmentCancellationToBarber(String barberEamil, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendFirstReminder(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendSecondReminder(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendCancellationNotification(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendAppointmentCancellationToClient(String to, AppointmentResponseDTO appointment);

}
