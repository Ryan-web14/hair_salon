package com.sni.hairsalon.service.serviceImpl;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Override
    @Async
    public CompletableFuture<Boolean> sendAppointmentConfirmation(String to, AppointmentResponseDTO appointment) {
        String subject = "Confirmation de rendez-vous";
        
        String body = String.format("""
            ********************************************
                    CONFIRMATION DE RENDEZ-VOUS
            ********************************************
            
            Bonjour Madame, Monsieur %s
            
            Votre rendez-vous a été confirmé:
            
            → Date: %s
            → Heure: %s
            → Barbier: %s
            → ID de rendez-vous: %s
            
            Prière d'arriver 5 à 15 min en avance.
            ----------------------------------------
            Merci de nous faire confiance!
            À très bientôt!
            ----------------------------------------

            IMPORTANT: En cas de reprogrammation, il est 
            préferable de s'y prendre 12 à 24h en avance 
            ou de nous contacter.

            Ne pas répondre. Générer automatiquement.
            """,
            appointment.getClientFirstname(),
            appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("d MMMM yyyy")),
            appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")),
            appointment.getBarberFirstname(),
            appointment.getId()
        );
        
        return sendEmail(to, subject, body);
    }

    @Override
    @Async
    public CompletableFuture<Boolean> sendFirstReminder(String to, AppointmentResponseDTO appointment){

        String subject = "Rappel de rendez-vous";
        String text = String.format ("""
  ********************************************
           RAPPEL DE RENDEZ-VOUS URGENT
   ********************************************
   
   Bonjour Madame, Monsieur %s
   
   ⚠️ ATTENTION ⚠️
   
   Votre rendez-vous était prévu pour: %s
   (Il y a 5 minutes)
   
   ----------------------------------------
   IMPORTANT: Votre rendez-vous sera 
   automatiquement annulé dans 10 minutes 
   si vous ne vous présentez pas.
   ----------------------------------------
   
   En cas de retard, merci de nous contacter 
   au plus vite.
   
   Cordialement,
   L'équipe du barbershop L'homme
    
   Ne pas répondre. Générer automatiquement.
        """,
        appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        return sendEmail(to, subject, text);
    }

    @Override
    @Async
    public CompletableFuture<Boolean> sendSecondReminder(String to, AppointmentResponseDTO appointment){
        
        String subject = "Rappel final de rendez-vous";
        String content = String.format(
            """
  ********************************************
           RAPPEL DE RENDEZ-VOUS URGENT
   ********************************************
   
    Madame, Monsieur %s
   
   ⚠️ ATTENTION ⚠️
   
   Votre rendez-vous était prévu pour: %s
   (Il y a 10 minutes)
   
   ----------------------------------------
   IMPORTANT: Votre rendez-vous sera 
   automatiquement annulé dans 5 minutes 
   si vous ne vous présentez pas.
   Ceci est le dernier rappel.
   ----------------------------------------
   

   Cordialement,
   L'équipe du barbershop L'homme

   Ne pas répondre. Générer automatiquement.
                    """,
    appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")));

    return sendEmail(to, subject, content);
    }

    @Override
    @Async
    public CompletableFuture<Boolean> sendCancellationNotification(String to, AppointmentResponseDTO appointment){
        
        String subject = "Annulation de rendez-vous";
        String content = String.format(
            """
   ********************************************
           ANNULATION DE RENDEZ-VOUS URGENT
   ********************************************
   
    Madame, Monsieur %s
   
   Votre rendez-vous pour %s du %s a été annulé,
   pour cause de retard.
    
   ----------------------------------------
   IMPORTANT: Cette annulation pour cause 
   de retard compte comme une non présence.
   A la troisième annulation, votre compte
   sera bloqué
   ----------------------------------------

   Cordialement,
   L'équipe du barbershop L'homme

   Ne pas répondre. Générer automatiquement.
   """, 
    appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")),
    appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("d MMMM yyyy")));

    return sendEmail(to, subject, content);
    }

    @Override
    @Async
    public CompletableFuture<Boolean> sendAppointmentCancellationToClient(String to,
     AppointmentResponseDTO appointment){

        String subject = "Annulation de rendez-vous";
        String content =String.format("""
    ********************************************
           ANNULATION DE RENDEZ-VOUS URGENT
    ********************************************
   
    Madame, Monsieur %s
   
   Votre rendez-vous pour %s du %s a été annulé,
   pour cause de retard.
    
   ----------------------------------------
   IMPORTANT: Si vous désirez un rendez-vous
   prière de vous rendre sur notre site.
   ----------------------------------------

   Cordialement,
   L'équipe du barbershop L'homme

   Ne pas répondre. Générer automatiquement.            
                """,
    appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")),
    appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
   );
    
    return sendEmail(to, subject, content);
     }


    @Override
    @Async
    public CompletableFuture<Boolean> sendAppointmentCancellationToBarber(String barberEamil,
     AppointmentResponseDTO appointment){

        String subject = "Notification d'annulation de rendez-vous";
        String content = String.format(
            """
             ********************************************
               NOTIFICATION D'ANNULATION DE RENDEZ-VOUS
             *******************************************
   
             Votre rendez-vous avec %s %s
             pour %s du %s a été annulé,
    
             ----------------------------------------
             IMPORTANT: Cette annulation pour cause 
             de retard compte comme une non présence.
             A la troisième annulation, votre compte
             sera bloqué
             ----------------------------------------

             Cordialement,
             L'équipe du barbershop L'homme

             Ne pas répondre. Générer automatiquement.        
                    """, 
                    appointment.getClientLastname(),
                    appointment.getClientFirstname(),
                    appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("d MMMM yyyy")));

            return sendEmail(barberEamil, subject, content);
     }


    

    @Async
    private CompletableFuture<Boolean> sendEmail(String to, String subject, String body){

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
    
    @Async
    private CompletableFuture<Boolean> sendHtmlEmail(String to, String subject, String content){
        try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true); // true indicates HTML content
        
        mailSender.send(message);
        return CompletableFuture.completedFuture(true);
    } catch (Exception e) {
        return CompletableFuture.completedFuture(false);
    }
    }



}


/*
public CompletableFuture<Boolean> sendAppointmentCancellationToBarber(String barberEamil);

public CompletableFuture<Boolean> sendDailyScheduleToBarber(String barberEmail, List<AppointmentResponseDTO> appointments);
 */