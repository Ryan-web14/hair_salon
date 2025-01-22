package com.sni.hairsalon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.dto.request.AppointmentRequestDTO;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.service.serviceImpl.AppointmentServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    
    private final AppointmentServiceImpl appointmentService;

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CLIENT')")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
        @RequestBody AppointmentRequestDTO request
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(appointmentService.createAppointment(request));
    }
}
