package com.sni.hairsalon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.service.SessionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/session")
@RequiredArgsConstructor
public class SessionController {
    
    private final SessionService sessionService;


    @DeleteMapping("/delete")
     @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteAllSession(){
        
        sessionService.deleteAllSession();
        return ResponseEntity.noContent().build();
    }
}
