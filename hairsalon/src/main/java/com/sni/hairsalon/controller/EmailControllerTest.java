package com.sni.hairsalon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}