package com.sni.hairsalon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.service.BarberService;
import com.sni.hairsalon.service.ClientService;
import com.sni.hairsalon.service.EstheticianService;
import com.sni.hairsalon.service.serviceImpl.BarberServiceImpl;
import com.sni.hairsalon.service.serviceImpl.ClientServiceImpl;
import com.twilio.http.Response;
import com.sni.hairsalon.model.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/me")
@RequiredArgsConstructor
public class ProfileController {
    
    private final ClientService clientService;
    private final BarberService barberService;
    private final EstheticianService estheticianService;

    @GetMapping
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserPrincipal authenticatedUser){
        
        String email = authenticatedUser.getUsername();
        String role = authenticatedUser.getAuthorities().iterator().next().getAuthority();

        if(role.equals("ROLE_CLIENT")){
            return ResponseEntity.ok().body(clientService.getClientProfile(email));
        }

        throw new IllegalStateException("not accessible");
    }

    @GetMapping("/staff")
    public ResponseEntity<?> getStaffProfile(@AuthenticationPrincipal UserPrincipal authenticateUser){
        String email = authenticateUser.getUsername();
        String role = authenticateUser.getAuthorities().iterator().next().getAuthority();
        
        if(role.equals("ROLE_BARBER")){
            return ResponseEntity.ok().body(barberService.getBarberProfile(email));
        }else if(role.equals("ROLE_ESTHETICIAN")){
            return ResponseEntity.ok().body(estheticianService.getEstheticianProfile(email)); 
        }
        throw new IllegalStateException("not accessible");
    }


}

