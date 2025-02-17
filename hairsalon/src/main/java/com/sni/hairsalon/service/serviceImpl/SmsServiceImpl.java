package com.sni.hairsalon.service.serviceImpl;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sni.hairsalon.model.Appointment;
import com.sni.hairsalon.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService{

    @Value("${twilio.account.sid}")
    public String twilioAccountSid;

    @Value("${twilio.auth.token}")
    public String twilioAuthToken;

    @Override
    public String sendConfirmationSms(Appointment appointment){

        String toNumber = "+2420" + String.valueOf(appointment.getClient().getPhoneNumber());
        String fromNumber = "+16067160493";
        String content = String.format("""
            Bonjour Madame, Monsieur %s
        
            Votre rendez-vous a été confirmé:
            
            → Date: %s
            → Heure: %s
            → Barbier: %s
            → ID de rendez-vous: %s
            
            Prière d'arriver 5 à 15 min en avance.
            Merci de nous faire confiance!
            À très bientôt!

            IMPORTANT: En ca
            s de reprogrammation, il est 
            préferable de s'y prendre 12 à 24h en avance 
            ou de nous contacter.

            Ne pas répondre. Générer automatiquement.

                
                """, 
            appointment.getClient().getFirstname(),
            appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("d MMMM yyyy")),
            appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")),
            appointment.getBarber().getFirstname(),
            appointment.getId());

        Twilio.init(twilioAccountSid, twilioAuthToken);
        Message message = Message.creator(new com.twilio.type.PhoneNumber(toNumber),
        new com.twilio.type.PhoneNumber(fromNumber), content).create();

        return message.getBody();
    }
    
    //add method to the sms
}
