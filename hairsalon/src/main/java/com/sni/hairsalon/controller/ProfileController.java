package com.sni.hairsalon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.service.serviceImpl.BarberServiceImpl;
import com.sni.hairsalon.service.serviceImpl.ClientServiceImpl;
import com.sni.hairsalon.model.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("v1/me")
@RequiredArgsConstructor
public class ProfileController {
    
    public final ClientServiceImpl clientService;
    public final BarberServiceImpl barberService;

    @GetMapping
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserPrincipal authenticatedUser){
        
        String email = authenticatedUser.getUsername();
        String role = authenticatedUser.getAuthorities().iterator().next().getAuthority();

        if(role.equals("ROLE_CLIENT")){
            return ResponseEntity.ok().body(clientService.getClientProfile(email));
        }
        else if(role.equals("ROLE_BARBER")){
            return ResponseEntity.ok().body(barberService.getBarberProfile(email));
        }

        throw new IllegalStateException("not accessible");
    }
}
