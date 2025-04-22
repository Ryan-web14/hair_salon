package com.sni.hairsalon.service.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infobip.ApiCallback;
import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.ApiKey;
import com.infobip.BaseUrl;
import com.infobip.api.WhatsAppApi;
import com.infobip.model.WhatsAppBulkMessage;
import com.infobip.model.WhatsAppBulkMessageInfo;
import com.infobip.model.WhatsAppMessage;
import com.infobip.model.WhatsAppTemplateBodyContent;
import com.infobip.model.WhatsAppTemplateContent;
import com.infobip.model.WhatsAppTemplateDataContent;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.service.WhatsappService;

@Service
@Transactional
public class WhatsappServiceImpl implements WhatsappService {

    public void sendAppointmentConfirmation(AppointmentResponseDTO appointment) throws ApiException{

        ApiClient apiClient = ApiClient.forApiKey(ApiKey.from("7a8f567cee1b03a35a5c65f377acce9e-6cbe8780-9d73-471d-a573-6aea186bbdb4"))
        .withBaseUrl(BaseUrl.from("https://e5lx33.api.infobip.com"))
        .build();
        WhatsAppApi whatsAppApi = new WhatsAppApi(apiClient);
        WhatsAppTemplateContent content = new WhatsAppTemplateContent();

        if(!appointment.getBarberId().isEmpty()){
            
        content.language("fr")
        .templateName("appointment_confirmation")
        .templateData(new WhatsAppTemplateDataContent()
        .body(new WhatsAppTemplateBodyContent()
        .addPlaceholdersItem(appointment.getClientFirstname())
        .addPlaceholdersItem(formatDateTime(appointment.getAppointmentTime(),"EEEE dd MMM yyyy" ))
        .addPlaceholdersItem(formatDateTime(appointment.getAppointmentTime(), "'HH''h''mm'"))
        .addPlaceholdersItem("Barbier")
        .addPlaceholdersItem(appointment.getBarberFirstname())
        .addPlaceholdersItem(appointment.getId())
        ));
        }

        if(!appointment.getEstheticianId().isEmpty()){
            content.language("fr")
        .templateName("appointment_confirmation")
        .templateData(new WhatsAppTemplateDataContent()
        .body(new WhatsAppTemplateBodyContent()
        .addPlaceholdersItem(appointment.getClientFirstname())
        .addPlaceholdersItem(formatDateTime(appointment.getAppointmentTime(),"EEEE dd MMM yyyy" ))
        .addPlaceholdersItem(formatDateTime(appointment.getAppointmentTime(), "'HH''h''mm'"))
        .addPlaceholdersItem("Esthéticienne")
        .addPlaceholdersItem(appointment.getBarberFirstname())
        .addPlaceholdersItem(appointment.getId())
        ));
        }
    
        
        WhatsAppBulkMessage bulkMessage = new WhatsAppBulkMessage()
        .addMessagesItem(new WhatsAppMessage()
                .from("242040451212")
                .to("242055645569")
                .content(content)
        );

       whatsAppApi.sendWhatsAppTemplateMessage(bulkMessage)
       .executeAsync(new ApiCallback<WhatsAppBulkMessageInfo>() {
        @Override
        public void onSuccess(WhatsAppBulkMessageInfo result, int responseStatusCode,
                Map<String, List<String>> responseHeaders) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onFailure(ApiException exception, int responseStatusCode,
                Map<String, List<String>> responseHeaders) {
            // TODO Auto-generated method stub
            
        }
       });
        return;
}

 /**
     * Format a LocalDateTime to string using the specified pattern
     * 
     * @param dateTime The LocalDateTime to format
     * @param pattern The pattern to use for formatting
     * @return The formatted date time string
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.FRANCE);
        return dateTime.format(formatter);
    }
}