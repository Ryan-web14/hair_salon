package com.sni.hairsalon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
import com.infobip.model.WhatsAppTemplateImageHeaderContent;
import com.infobip.model.WhatsAppTemplateQuickReplyButtonContent;
import com.infobip.model.WhatsAppTextMessage;




@Controller
@RequestMapping("/v1/whatsapp")
public class WhatsappTest {


    @PostMapping("/send")
    ResponseEntity<Void> sendWhatsapp() throws ApiException{
        ApiClient apiClient = ApiClient.forApiKey(ApiKey.from("7a8f567cee1b03a35a5c65f377acce9e-6cbe8780-9d73-471d-a573-6aea186bbdb4"))
        .withBaseUrl(BaseUrl.from("https://e5lx33.api.infobip.com"))
        .build();
        WhatsAppApi whatsAppApi = new WhatsAppApi(apiClient);
        
        // WhatsAppTemplateContent content = new WhatsAppTemplateContent()
        // .language("fr")
        // .templateName("appointment_confirmation")
        // .

        WhatsAppTemplateContent content = new WhatsAppTemplateContent()
        .language("fr")
        .templateName("appointment_confirmation")
        .templateData(new WhatsAppTemplateDataContent()
        .body(new WhatsAppTemplateBodyContent()
        .addPlaceholdersItem("Joel")
        .addPlaceholdersItem("28 avril 2025")
        .addPlaceholdersItem("15:30")
        .addPlaceholdersItem("Barbier")
        .addPlaceholdersItem("Mboula")
        .addPlaceholdersItem("5887459963255"))
        )
        ;
        
        WhatsAppBulkMessage bulkMessage = new WhatsAppBulkMessage()
        .addMessagesItem(new WhatsAppMessage()
                .from("242040451212")
                .to("242055645569")
                .content(content)
        );

    WhatsAppBulkMessageInfo messageInfo = whatsAppApi
        .sendWhatsAppTemplateMessage(bulkMessage)
        .execute();

    System.out.println(messageInfo.getMessages().get(0).getStatus().getDescription());

            return ResponseEntity.noContent().build();
    }
    
}


