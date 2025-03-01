package com.sni.hairsalon.service.serviceImpl;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.model.Client;
import com.sni.hairsalon.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
/*import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;*/

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
                    CONFIRMATION DE RENDEZ-VOUS
            
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
    public CompletableFuture<Boolean> sendWelcomeMessage(Client client){
        String subject = "Bienvenu";
        String email = client.getUser().getEmail();
        String body = String.format("""
            
            Bonjour Madame, Monsieur %s
            
            Cet email confirme la création de votre compte
            et nous souhaitons la bienvenue chez l'homme.

            ID client: %s
            Nom: %s
            Prénom: %s
            Email: %s

            Au plaisir de vous avoir au plus vite
            Merci de nous faire confiance!
            À très bientôt !

            L'équipe de L'HOMME

            Ne pas répondre. Générer automatiquement.
            """,
            client.getFirstname(),
            String.valueOf(client.getId()),
            client.getFirstname(),
            client.getLastname(),
            email
         );
        
        return sendEmail(email, subject, body);
    }

    @Override
    @Async
    public CompletableFuture<Boolean> sendFirstReminder(String to, AppointmentResponseDTO appointment){

        String subject = "Rappel de rendez-vous";
        String text = String.format ( """
   RAPPEL DE RENDEZ-VOUS URGE
   
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
   L'équipe de L'HOMME
    
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
                    
    RAPPEL DE RENDEZ-VOUS URGE
   
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
   L'équipe de L'HOMME

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
    ANNULATION DE RENDEZ-VOUS URGENT
   
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
   L'équipe de L'HOMME

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
    ANNULATION DE RENDEZ-VOUS URGENT
   
    Madame, Monsieur %s
   
   Votre rendez-vous pour %s du %s a été annulé,
   pour cause de retard.
    
   ----------------------------------------
   IMPORTANT: Si vous désirez un rendez-vous
   prière de vous rendre sur notre site.
   ----------------------------------------

   Cordialement,
   L'équipe de L'HOMME

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
            NOTIFICATION D'ANNULATION DE RENDEZ-VOUS
   
             Votre rendez-vous avec %s %s
             pour %s du %s a été annulé,
    
             ----------------------------------------
             IMPORTANT: Cette annulation pour cause 
             de retard compte comme une non présence.
             A la troisième annulation, votre compte
             sera bloqué
             ----------------------------------------

             Cordialement,
             L'équipe de L'HOMME

             Ne pas répondre. Générer automatiquement.        
                    """, 
                    appointment.getClientLastname(),
                    appointment.getClientFirstname(),
                    appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("d MMMM yyyy")));

            return sendEmail(barberEamil, subject, content);
     }

     @Override
     @Async
     public CompletableFuture<Boolean> sendDailyScheduleToBarber(String barberEmail,
      List<Appointment> appointments){

        String subject = "Votre programme pour le " + 
        LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d"));

        StringBuilder schedule =  new StringBuilder();
        schedule.append("Voici votre programme de la journée: ");

        appointments.stream()
        .map(apt->schedule.append(String.format(
            
        """
                %s - %s
                    Client: %s %s
                    Service: %s
                    Duration: %d minutes

                """, 
                apt.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                apt.getAppointmentTime().plusMinutes(apt.getHaircut().getDuration())
                    .format(DateTimeFormatter.ofPattern("HH:mm")),
                apt.getClient().getLastname(),
                apt.getClient().getFirstname(),
                apt.getHaircut().getType(),
                apt.getHaircut().getDuration())));

            schedule.append("Passez une bonne journée");

            return sendEmail(barberEmail, subject, schedule.toString());

     }

     @Override
     @Async
     public CompletableFuture<Boolean> sendCheckIn( Appointment appointment){
         
        String subject = "Notification arrivé de client";
        String content = String.format(
            """
               NOTIFICATION PRESENCE DE CLIENT
   
             Le client %s est arrivé au salon de coiffure
             pour son rendez-vous.

             Détail de rendez-vous:
             Heure: %s
             Service: %s
             Durée: %d minutes
    
             ----------------------------------------
             IMPORTANT: Respecter le temps de coiffure.
             En cas de dépassement notifier le manager
             ----------------------------------------

             Ne pas répondre. Générer automatiquement.   
                    """,
                    appointment.getClient().getFirstname(),
                    appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    appointment.getHaircut().getType(),
                    appointment.getHaircut().getDuration()
        );

        String email =  appointment.getBarber().getUser().getEmail();

        return sendEmail(email, subject, content);
     }

    @Override
    @Async
    public CompletableFuture<Boolean> sendCheckInNotification(String barberEmail, Appointment appointment){
        String subject = "Notification d'arrivée de client";
        String content = String.format(
            """
               NOTIFICATION PRESENCE DE CLIENT
         
             Le client %s %s est arrivé au salon de coiffure
             pour son rendez-vous.
         
             Détail de rendez-vous:
             Heure: %s
             Service: %s
             Durée: %d minutes
         
             ----------------------------------------
             IMPORTANT: Respecter le temps de coiffure.
             En cas de dépassement notifier le manager
             ----------------------------------------
         
             Ne pas répondre. Générer automatiquement.
            """,
            appointment.getClient().getLastname(),
            appointment.getClient().getFirstname(),
            appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")),
            appointment.getHaircut().getType(),
            appointment.getHaircut().getDuration()
        );

        return sendEmail(barberEmail, subject, content);
    }
    
    @Override
    @Async
    public CompletableFuture<Boolean> sendPasswordResetEmail(String email, String link){

        String subject = "Réinitialisation de mot de passe";
        String content = String.format("Cliquez sur le lien pour réinitialiser votre mot de passe: %s",
         link);

         return sendEmail(email, subject, content);
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

//Testing function

   /* public CompletableFuture<Boolean> sendAppointmentConfirmationWithICSFile(
        String to, AppointmentResponseDTO appointment
    ){
        try{

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Confirmation de rendez vous");
            helper.setText(createFormattedContent(appointment));

        }catch(Exception e){
            e.getMessage();
            return CompletableFuture.completedFuture(false);
        }
    }

    private byte[] generateICSFile(AppointmentResponseDTO appointment){

        try {
            net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
             calendar.getProperties().add(new ProdId("-//Hairsalon//Appointment//FR"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        
        Date date = Date
        // Create start date and duration
        Date start = Date.of(appointment.getAppointmentTime());
        Duration duration = new Duration(java.time.Duration.ofMinutes(30));

        // Create event
        VEvent event = new VEvent(start, duration, 
            "Rendez-vous coiffeur avec " + appointment.getBarberFirstname());

        // Add alarm
        VAlarm alarm = new VAlarm(java.time.Duration.ofMinutes(-30));
        alarm.getProperties().add(Action.DISPLAY);
        alarm.getProperties().add(new Description("Rappel de rendez-vous"));
        event.getAlarms().add(alarm);

        calendar.getComponents().add(event);

        return calendar.toString().getBytes();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }



    private String createFormattedContent(AppointmentResponseDTO appointment) {
        return String.format("""
                    CONFIRMATION DE RENDEZ-VOUS
    
            Bonjour %s,
    
            Votre rendez-vous a été confirmé:
    
            → Date: %s
            → Heure: %s
            → Barbier: %s
            → ID: %d
    
            ----------------------------------------
            Un fichier calendar (.ics) est joint à 
            cet email pour ajouter le rendez-vous 
            à votre calendrier.
            ----------------------------------------
    
            Merci de nous faire confiance!
            À très bientôt!
            """,
            appointment.getClientFirstname(),
            appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("d MMMM yyyy")),
            appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")),
            appointment.getBarberFirstname(),
            appointment.getId()
        );
    }*/
}


/*
public CompletableFuture<Boolean> sendAppointmentCancellationToBarber(String barberEamil);

public CompletableFuture<Boolean> sendDailyScheduleToBarber(String barberEmail, List<AppointmentResponseDTO> appointments);
 */