package com.sni.hairsalon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.dto.request.UserRequestDTO;
import com.sni.hairsalon.dto.response.AuthResponse;
import com.sni.hairsalon.dto.response.SessionResponseDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.service.AuthentificationService;
import com.sni.hairsalon.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/auth")
public class AuthentificationController {
    
    private final AuthentificationService authService;
    private final SessionService sessionService;

    public AuthentificationController(AuthentificationService authService,SessionService sessionService ){
        this.authService = authService;
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (
       @Validated @RequestBody UserRequestDTO loginRequest, 
        HttpServletRequest request
    ) throws ResourceNotFoundException{
        return ResponseEntity.ok(authService.login(loginRequest, request));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(
     @RequestBody UserRequestDTO signupRequest){
            return  ResponseEntity.status(HttpStatus.CREATED)
            .body(authService.signUp(signupRequest));
        }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @RequestHeader("Authorization") String bearerToken){
            if(bearerToken != null &&  bearerToken.startsWith("Bearer ")){
                String token = bearerToken.substring(7);
                authService.logout(token);
            }
            return ResponseEntity.ok().build();
        }

       @GetMapping("/session")
           public ResponseEntity<SessionResponseDTO> getCurrentSession(
            @RequestHeader("Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            return ResponseEntity.ok(sessionService.getCurrentSession(token));
        }
        return ResponseEntity.badRequest().build();
    }
  }

