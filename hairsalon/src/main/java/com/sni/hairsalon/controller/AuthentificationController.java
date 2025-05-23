package com.sni.hairsalon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.sni.hairsalon.dto.response.UserResponseDTO;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.service.AuthentificationService;
import com.sni.hairsalon.service.EmailService;
import com.sni.hairsalon.service.PasswordResetTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthentificationController {
    
    private final AuthentificationService authService;
    private final PasswordResetTokenService passwordResetService;
    private final EmailService mailService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (
       @Validated @RequestBody UserRequestDTO loginRequest, 
        HttpServletRequest request
    ) throws ResourceNotFoundException{
        return ResponseEntity.ok(authService.login(loginRequest, request));
    }

    @PostMapping("/login/admin")
    public ResponseEntity<AuthResponse> loginAdmin(@Validated @RequestBody UserRequestDTO dto, HttpServletRequest request){
        return ResponseEntity.ok(authService.loginAdmin(dto,request));
    }

    @PostMapping("/login/staff")
    public ResponseEntity<AuthResponse> loginStaff(@Validated @RequestBody UserRequestDTO dto, HttpServletRequest request){
        return ResponseEntity.ok(authService.loginStaff(dto, request));
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


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
        
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

    @PostMapping("/admin/signup/client")
    public ResponseEntity<ClientSignupResponse> signupClientByAdmin(@RequestBody ClientSignupRequest request){
        
        String token = passwordResetService.createToken(request.getEmail());

        String passwordLink = "https://lhomme-cg.com/change-password?token=" + token;

       return ResponseEntity.ok().body(authService.signupClientByAdmin(request, passwordLink));
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<UserResponseDTO> signupAdmin(@RequestBody UserRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(authService.signupAdmin(request));
    }

    
    @PostMapping("/signup/manager")
    public ResponseEntity<UserResponseDTO> signupManager(@RequestBody UserRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(authService.signupManager(request));
    }


    @PostMapping("/change-password")
    public ResponseEntity<?> changeTemporaryPassword(@RequestBody ResetPasswordRequestDTO resetRequest){
        String token = resetRequest.getToken();
        String newPassword = resetRequest.getNewPassword();

        passwordResetService.validateToken(token);
        passwordResetService.resetPassword(token, newPassword);

        return ResponseEntity.ok("Changement effectué");
    }
  }

