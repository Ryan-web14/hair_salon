package com.sni.hairsalon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.dto.request.BarberRequestDTO;
import com.sni.hairsalon.dto.response.BarberResponseDTO;
import com.sni.hairsalon.service.serviceImpl.BarberServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/barber")
@RequiredArgsConstructor
public class BarberController {
    
    private final BarberServiceImpl barberService;

    @PostMapping("/")
    public ResponseEntity<BarberResponseDTO> createBarber(@RequestBody BarberRequestDTO request){

        return ResponseEntity.status(HttpStatus.CREATED)
        .body(barberService.createBarber(request));
     }
}
