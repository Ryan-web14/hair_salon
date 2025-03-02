package com.sni.hairsalon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.dto.request.AppointmentRequestDTO;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.exception.BadRequestException;
import com.sni.hairsalon.model.UserPrincipal;
import com.sni.hairsalon.service.AppointmentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    
    private final AppointmentService appointmentService;

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CLIENT')")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
        @RequestBody AppointmentRequestDTO request
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(appointmentService.createAppointment(request));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointment(){

        return ResponseEntity.ok().body(appointmentService.getAllAppointment());
        
    }

    @GetMapping("/completed")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyCompletedAppointment(@AuthenticationPrincipal 
    UserPrincipal authenticatedUser){
       
       try{
        String email = authenticatedUser.getUsername();
        String role = authenticatedUser.getAuthorities().iterator().next().getAuthority();
        
        if(role.equals("ROLE_CLIENT"))
        {
           return ResponseEntity.ok().body(appointmentService.getClientCompletedAppointment(email));
        }

        throw new IllegalStateException("not accessible");
    }catch(Exception e){
        throw new BadRequestException(e.getMessage());
       
    }
    }
    
   /*PostMapping("/checkIn")
    public ResponseEntity<Void> checkInClient(@RequestParam) */ 

    }



