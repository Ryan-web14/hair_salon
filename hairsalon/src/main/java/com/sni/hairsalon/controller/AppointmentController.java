package com.sni.hairsalon.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sni.hairsalon.dto.request.AppointmentRequestDTO;
import com.sni.hairsalon.dto.response.AppointmentResponseDTO;
import com.sni.hairsalon.exception.BadRequestException;
import com.sni.hairsalon.model.UserPrincipal;
import com.sni.hairsalon.service.AppointmentService;
import com.twilio.http.Response;

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

        throw new IllegalAccessError("No access");
    }catch(Exception e){
        throw new BadRequestException(e.getMessage());
       
    }
 }

    @GetMapping("/admin/completed")
    @PreAuthorize("hasRole('ADMIN') or hasRole(MANAGER)")
    public ResponseEntity<List<AppointmentResponseDTO>> getCompletedAppointment(String email){

        return ResponseEntity.ok().body(appointmentService.getClientCompletedAppointment(email));

    }

    @PostMapping("/{appointmentId}/status/completed")
    @PreAuthorize("hasRole('ADMIN') or hasRole(MANAGER)")
    public ResponseEntity<Void> completeAppointment(@PathVariable long appointmentId){

        appointmentService.makeAppointmentCompleted(appointmentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkIn")
    public ResponseEntity<Void> checkInClient(@RequestParam String email){

        appointmentService.checkIn(email);
        return ResponseEntity.noContent().build();
    } 

    @GetMapping("/barber/admin/date")
    @PreAuthorize("hasRole('ADMIN') or hasRole(MANAGER)")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllBarberAppointment( 
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){

            return ResponseEntity.ok().body(appointmentService.getAllBarberAppointment(date));
        }

    @GetMapping("/for-barber")
    @PreAuthorize("hasRole('BARBER')")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyBarberAppointment(
    @AuthenticationPrincipal UserPrincipal authenticatedBarber){
        try{
            String email = authenticatedBarber.getUsername();
        String role = authenticatedBarber.getAuthorities().iterator().next().getAuthority();

        if(role.equals("BARBER")){

            return ResponseEntity.ok().body(appointmentService.getMyBarberAppointment(email));
        }
        throw new IllegalAccessError("No access");
        }catch(Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }

    @PostMapping("/{appointmentId}/cancel")
    public ResponseEntity<Void> cancelAppointmentByClient(@PathVariable long id, @RequestParam String clientEmail, @AuthenticationPrincipal 
    UserPrincipal authenticatedUser){
        try{
            String email =   authenticatedUser.getUsername();
            String role = authenticatedUser.getAuthorities().iterator().next().getAuthority();
            
            if(role.equals("ROLE_CLIENT"))
            {
               appointmentService.cancelAppointmentByClient(id,email);
               return ResponseEntity.noContent().build();
            }
    
            throw new IllegalAccessError("No access");
        }catch(Exception e){
            throw new BadRequestException(e.getMessage());
        }
  }

  @PutMapping("/{appointmentId}/admin/cancel")
  @PreAuthorize("hasRole('ADMIN') or hasRole(MANAGER)")
  public ResponseEntity<Void> cancelAppointment(@PathVariable long appointmentId){

    appointmentService.cancelAppointment(appointmentId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/search/{status}/name")
  @PreAuthorize("hasRole('ADMIN') or hasRole(MANAGER)")
  public ResponseEntity<List<AppointmentResponseDTO>> searchAppointment(@PathVariable Integer status,
   @RequestParam String name){

    return ResponseEntity.ok().body(appointmentService.searchAppointmentByClientName(name, status));
  }

  @PutMapping("/{appointmentId}/admin/update")
  @PreAuthorize("hasRole('ADMIN') or hasRole(MANAGER)")
  public ResponseEntity<AppointmentResponseDTO> updateAppointmentByAdmin(@PathVariable long appointmentId, 
  @RequestBody AppointmentRequestDTO request){
    
    return ResponseEntity.ok().body(appointmentService.updateAppointmentByAdmin(appointmentId, request));
  }
}


