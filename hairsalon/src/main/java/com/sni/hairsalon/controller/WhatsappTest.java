package com.sni.hairsalon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infobip.ApiException;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.service.WhatsappService;

import lombok.RequiredArgsConstructor;




@Controller
@RequestMapping("/v1/whatsapp")
@RequiredArgsConstructor
public class WhatsappTest {

    private final WhatsappService whatsappService;

    @PostMapping("/send")
    ResponseEntity<Void> sendWhatsapp() throws ApiException{

        AppointmentResponseDTO appointment = new AppointmentResponseDTO();

        whatsappService.sendAppointmentConfirmation(appointment);
            return ResponseEntity.noContent().build();
    }
    
}


