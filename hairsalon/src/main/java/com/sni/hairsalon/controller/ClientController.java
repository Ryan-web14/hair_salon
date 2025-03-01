package com.sni.hairsalon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.dto.request.ClientRequestDTO;
import com.sni.hairsalon.dto.response.ClientResponseDTO;
import com.sni.hairsalon.model.UserPrincipal;
import com.sni.hairsalon.service.serviceImpl.ClientServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/client")
@RequiredArgsConstructor
public class ClientController {
    
    private final ClientServiceImpl clientService;

    @PostMapping("/")
    public ResponseEntity<ClientResponseDTO> createClient(
    @RequestBody ClientRequestDTO client){
        
        return ResponseEntity.status(HttpStatus.CREATED).
        body(clientService.createClient(client));

    }

    @PutMapping("/update")
    public ResponseEntity<ClientResponseDTO> updateClient(@AuthenticationPrincipal UserPrincipal authenticatedUser,
    @RequestBody ClientRequestDTO request){
        String email = authenticatedUser.getUsername();
        return ResponseEntity.ok().body(clientService.updateClient(email, request));
    }

}
