package com.sni.hairsalon.service.serviceImpl;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Override
    @Async
    public CompletableFuture<Boolean> sendAppointmentConfirmation(String to, AppointmentResponseDTO appointment ){

        String subject = "Confirmation de rendez-vous";
        String body = String.format("""
                  Bonjour %s,

        Votre rendez vous a été confirmé pour:
        
        Date: %s

        Heure: %s

        Barbier: %s

        ID de rendez-vous: %s

        Merci de nous faire confiance à très bientôt !
                """,
                appointment.getClientFirstname(),
                appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("d MMMM yyyy ")),
                appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                appointment.getBarberFirstname(),
                appointment.getId()
        );
        
        return sendEmail(appointment.getClientEmail(),
        subject,
        body);
    }

    @Async
    public CompletableFuture<Boolean> sendEmail(String to, String subject, String body){

        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            return CompletableFuture.completedFuture(true);
        }catch(MailException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to send mail");
        }
    }
    

}


/*
    public CompletableFuture<Boolean> sendAppointmentConfirmation(String to, AppointmentResponseDTO appointment );
    public CompletableFuture<Boolean> sendAppointmentCancellationToClient(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendDailyScheduleToBarber(String barberEmail, List<AppointmentResponseDTO> appointments);
    public CompletableFuture<Boolean> sendAppointmentCancellationToBarber(String barberEamil);
    public CompletableFuture<Boolean> sendFirstReminder(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendFSecondReminder(String to, AppointmentResponseDTO appointment);
    public CompletableFuture<Boolean> sendCancellationNotification(String to, String barberEmail); */