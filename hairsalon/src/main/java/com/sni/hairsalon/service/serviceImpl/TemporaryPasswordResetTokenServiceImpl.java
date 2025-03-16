package com.sni.hairsalon.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sni.hairsalon.exception.InvalidTokenException;
import com.sni.hairsalon.exception.ResourceNotFoundException;
import com.sni.hairsalon.model.TemporaryPasswordResetToken;
import com.sni.hairsalon.repository.TemporaryPasswordResetTokenRepository;
import com.sni.hairsalon.service.TemporaryPasswordResetTokenService;
import com.sni.hairsalon.service.UserService;
import com.sni.hairsalon.utils.ValidationUtils;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class TemporaryPasswordResetTokenServiceImpl implements TemporaryPasswordResetTokenService {

    private final TemporaryPasswordResetTokenRepository passwordRepo;
    private final UserService userService;
    private static final long EXPIRATTION_TIME = 1460;


    public String createToken(String email, String password){

          if(!ValidationUtils.isValidEmail(email)){
            throw new RuntimeException("Invalid email");
        }

        passwordRepo.deleteByEmail(email);
        String token = UUID.randomUUID().toString();
        TemporaryPasswordResetToken temporaryToken = TemporaryPasswordResetToken.builder()
        .email(email)
        .token(token)
        .password(password)
        .creationDate(LocalDateTime.now().plusHours(1))
        .expirationDate(LocalDateTime.now().plusHours(1).plusMinutes(EXPIRATTION_TIME))
        .build();

        passwordRepo.save(temporaryToken);

        return token;
    }

    
    public TemporaryPasswordResetToken validateToken(String tempToken){

        TemporaryPasswordResetToken token  = passwordRepo.findByToken(tempToken)
        .orElseThrow(()-> new ResourceNotFoundException("token not found" + tempToken));

      
        if(token.isExpired()){
            throw new InvalidTokenException("Token is expired");
        }

        if(token.isUsed()){
            throw new InvalidTokenException("Token already used");
        }

        return token;

    }
    
    public void resetPassword(String token, String password){

        return;
    } 
    
}


