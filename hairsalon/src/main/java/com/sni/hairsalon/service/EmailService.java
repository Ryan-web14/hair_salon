package com.sni.hairsalon.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.model.Client;

public interface EmailService {

    public CompletableFuture<Boolean> sendAppointmentConfirmation(String to, AppointmentResponseDTO appointment );
    public CompletableFuture<Boolean> sendCheckInNotification(String barberEmail, Appointment appointment);
    public CompletableFuture<Boolean> sendDailyScheduleToBarber(String barberEmail, List<Appointment> appointments);
    public CompletableFuture<Boolean> sendAppointmentCancellationToBarber(String barberEamil, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendFirstReminder(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendSecondReminder(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendCancellationNotification(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendAppointmentCancellationToClient(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendCheckIn(Appointment appointment);
     public CompletableFuture<Boolean> sendWelcomeMessage(Client client);
    
}
