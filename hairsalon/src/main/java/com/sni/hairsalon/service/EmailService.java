package com.sni.hairsalon.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;
import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.model.Client;

public interface EmailService {

    public CompletableFuture<Boolean> sendAppointmentConfirmation(String to, AppointmentResponseDTO appointment );
    public CompletableFuture<Boolean> sendCheckInNotification(String barberEmail, Appointment appointment);
    public CompletableFuture<Boolean> sendDailyScheduleToBarber(String barberEmail, Set<Appointment> appointments);
    public CompletableFuture<Boolean> sendDailyScheduleToEsthetician(String estheticianEmail,List<Appointment> appointments); 
    public CompletableFuture<Boolean> sendAppointmentCancellationToBarber(String barberEamil, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendFirstReminder(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendSecondReminder(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendCancellationNotification(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendAppointmentCancellationToClient(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendCheckIn(Appointment appointment);
    public CompletableFuture<Boolean> sendCheckInToEsthetician(Appointment appointment);
    public CompletableFuture<Boolean> sendWelcomeMessage(Client client);
    public CompletableFuture<Boolean> sendPasswordResetEmail(String email, String link);
    public CompletableFuture<Boolean> sendTemporaryPasswordChangeEmail(String email, String temporaryPassword, String link);
    public CompletableFuture<Boolean> sendBarberAccountInformation(String email,UserResponseDTO dto, String randomPassword);
    public CompletableFuture<Boolean> sendBarberNotificationOfNewAppointment(String barberEmail, Appointment appointment);
    public CompletableFuture<Boolean> sendRescheduleAppointmentToClient(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendRescheduleAppointmentToBarber(String to, Appointment appointment);
    public CompletableFuture<Boolean> sendEstheticianNotificationOfNewAppointment(String estheticianEmail, Appointment appointment);
    public CompletableFuture<Boolean> sendRescheduleAppointmentToEsthetician(String to, Appointment appointment);
    public CompletableFuture<Boolean> sendEstheticianAccountInformation(String email, UserResponseDTO dto, String randomPassword);
    public CompletableFuture<Boolean> sendAppointmentCancellationToEsthetician(String estheticianEmail,AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendAppointmentReminder(Appointment appointment);
}
