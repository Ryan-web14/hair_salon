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

import com.sni.hairsalon.dto.request.EstheticRequestDTO;
import com.sni.hairsalon.dto.response.EstheticResponseDTO;
import com.sni.hairsalon.service.EstheticService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/esthetic")
@RequiredArgsConstructor
public class EstheticController {
    
    private final EstheticService estheticService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstheticResponseDTO> createEsthetic(@RequestBody EstheticRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(estheticService.createEsthetic(dto));
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstheticResponseDTO> updateEsthetic(@PathVariable Long id, @RequestBody EstheticRequestDTO dto) {
        return ResponseEntity.ok(estheticService.updateEsthetic(id, dto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<EstheticResponseDTO>> getAllEsthetics() {
        return ResponseEntity.ok(estheticService.getAllEsthetics());
    }
    
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEsthetic(@PathVariable Long id) {
        estheticService.deleteEsthetic(id);
        return ResponseEntity.noContent().build();
    }
}