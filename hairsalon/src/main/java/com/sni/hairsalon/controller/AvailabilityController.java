package com.sni.hairsalon.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.dto.request.AvailabilityRequestDTO;
import com.sni.hairsalon.dto.response.AvailabilityResponseDTO;
import com.sni.hairsalon.model.UserPrincipal;
import com.sni.hairsalon.service.AvailabilityService;
import com.twilio.rest.api.v2010.account.availablephonenumbercountry.Local;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/availability")
@RequiredArgsConstructor
public class AvailabilityController {
    
    private final AvailabilityService availabilityService;

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AvailabilityResponseDTO>> createAvailability(
       @Validated @RequestBody AvailabilityRequestDTO request){

            return ResponseEntity.status(HttpStatus.CREATED)
            .body(availabilityService.createAvailability(request));
        }

    @PostMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<AvailabilityResponseDTO> updateAvailabilityStatus(
        @PathVariable Long id, @RequestParam boolean isAvailable){
            
            return ResponseEntity.ok(availabilityService.updateAvailabilityStatus(id, isAvailable));
        }

    @GetMapping("/barber/{barberId}/slot")
    public ResponseEntity<List<AvailabilityResponseDTO>> getBarberAvailability(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable Long barberId){
            
            return ResponseEntity.ok(availabilityService.getBarberAvailability(barberId, date));
        } 

        @GetMapping("/esthetician/{estheticianId}/slot")
public ResponseEntity<List<AvailabilityResponseDTO>> getEstheticianAvailability(
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
    @PathVariable Long estheticianId) {
        
    return ResponseEntity.ok(availabilityService.getEstheticianAvailability(estheticianId, date));
}

    @GetMapping("provider")
    public ResponseEntity<List<AvailabilityResponseDTO>> getProviderAvailability(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @AuthenticationPrincipal UserPrincipal authenticatedUSer
    ){
        return ResponseEntity.ok().body(availabilityService.getProviderAvailability(authenticatedUSer, date));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteAllAvailability(){
        availabilityService.deleteAllAvailability();;
        return ResponseEntity.noContent().build();
    }
}

