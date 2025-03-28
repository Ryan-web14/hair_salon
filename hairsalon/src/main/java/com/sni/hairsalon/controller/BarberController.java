package com.sni.hairsalon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.dto.request.BarberRequestDTO;
import com.sni.hairsalon.dto.response.BarberResponseDTO;
import com.sni.hairsalon.service.BarberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/barber")
@RequiredArgsConstructor
public class BarberController {
    
    private final BarberService barberService;

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<BarberResponseDTO> createBarberByAdmin(@RequestBody BarberRequestDTO request){

        return ResponseEntity.status(HttpStatus.CREATED)
        .body(barberService.createBarberByAdmin(request));
     }

     @GetMapping("/all")
     public ResponseEntity <List<BarberResponseDTO>> getAllBarber(){
        return ResponseEntity.ok().body(barberService.getAllBarber());
     }

     @GetMapping("/available")
     public ResponseEntity <List<BarberResponseDTO>> getAllAvailableBarber(){
        return ResponseEntity.ok().body(barberService.getAllAvailableBarber());
     }

     @PutMapping("/{id}/admin/update")
     @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
     public ResponseEntity<BarberResponseDTO> updateBarberByAdmin(@PathVariable Long id,@RequestBody BarberRequestDTO request){
         return ResponseEntity.ok().body(barberService.updateBarber(id, request));
     }

     @DeleteMapping("/{barberId}/admin/delete")
     public ResponseEntity<Void> deleteBarber(@PathVariable Long barberId){
       
       barberService.deleteBarber(barberId);
       return ResponseEntity.noContent().build();
     }

}
