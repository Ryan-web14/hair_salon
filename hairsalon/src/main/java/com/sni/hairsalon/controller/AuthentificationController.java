package com.sni.hairsalon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sni.hairsalon.dto.request.ClientSignupRequest;
import com.sni.hairsalon.dto.request.ResetPasswordRequestDTO;
import com.sni.hairsalon.dto.request.UserRequestDTO;
import com.sni.hairsalon.dto.response.AuthResponse;
import com.sni.hairsalon.dto.response.ClientSignupResponse;
import com.sni.hairsalon.dto.response.SessionResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.service.AuthentificationService;
import com.sni.hairsalon.service.SessionService;
import com.sni.hairsalon.service.serviceImpl.EmailServiceImpl;
import com.sni.hairsalon.service.serviceImpl.PasswordResetTokenServiceImpl;
import com.sni.hairsalon.service.serviceImpl.UserServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthentificationController {
    
    private final AuthentificationService authService;
    private final SessionService sessionService;
    private final UserServiceImpl userService;
    private final PasswordResetTokenServiceImpl passwordResetService;
    private final EmailServiceImpl mailService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (
       @Validated @RequestBody UserRequestDTO loginRequest, 
        HttpServletRequest request
    ) throws ResourceNotFoundException{
        return ResponseEntity.ok(authService.login(loginRequest, request));
    }

    @PostMapping("/signup/client")
    public ResponseEntity<ClientSignupResponse> signup(
     @RequestBody ClientSignupRequest signupRequest){
            return  ResponseEntity.status(HttpStatus.CREATED)
            .body(authService.signupClient(signupRequest));
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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
        
        if(!userService.emailAlreadyExist(email)){
            return ResponseEntity.ok("Vous recevrez un email si vous avez un compte");
        }

        String token = passwordResetService.createToken(email);

        String resetLink = "https://lhomme-cg.com/reset-password?token=" + token;

        mailService.sendPasswordResetEmail(email, resetLink);

        return ResponseEntity.ok("Verifier votre email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO resetRequest){

        String token = resetRequest.getToken();
        String newPassword = resetRequest.getNewPassword();

        passwordResetService.validateToken(token);
        passwordResetService.resetPassword(token, newPassword);

        return ResponseEntity.ok("mot de passe réinitialisé");
    }
  }

