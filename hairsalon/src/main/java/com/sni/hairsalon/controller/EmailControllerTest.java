/*package com.sni.hairsalon.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.service.serviceImpl.EmailServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/email/test")
@RequiredArgsConstructor
public class EmailControllerTest{

    private final EmailServiceImpl mailService;


    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(){

        try{
            mailService.sendEmail(
                "contact@sni-cg.com", 
                "test email",
                 "testing email for the app");

                 return ResponseEntity.ok("sending email");

        }catch(Exception e ){
            return ResponseEntity.status((HttpStatus.INTERNAL_SERVER_ERROR))
            .body(e.getMessage());
        }
    }

    @PostMapping("/html")
    public ResponseEntity<String> testHtmlEmail(@RequestParam String to) {
        String testHtmlContent = "<html><body><h1>Test</h1><p>Simple HTML test</p></body></html>";

        try {
            mailService.sendHtmlEmail(to, "Test HTML Email", testHtmlContent);
            return ResponseEntity.ok("Test email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to send email: " + e.getMessage());
        }
    }
}*/