package com.sni.hairsalon.service.serviceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.eclipse.angus.mail.handlers.message_rfc822;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sni.hairsalon.exception.InvalidTokenException;
import com.sni.hairsalon.model.PasswordResetToken;
import com.sni.hairsalon.repository.PasswordResetTokenRepository;
import com.sni.hairsalon.service.PasswordResetTokenService;
import com.sni.hairsalon.utils.ValidationUtils;

@Service
@Transactional
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService  {
    
    private PasswordResetTokenRepository pwdResetTokenRepo;
    private static final long EXPIRATTION_TIME = 30;

    public String createToken(String email){
        
        if(!ValidationUtils.isValidEmail(email)){
            throw new RuntimeException("Invalid email");
        }
        //delete any existing token for this email when trying to create a new one
        pwdResetTokenRepo.deleteByEmail(email);
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
        .token(token)
        .email(email)
        .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
        .expirationDate(LocalDateTime.now().plusMinutes(EXPIRATTION_TIME))
        .build();

        pwdResetTokenRepo.save(resetToken);
        return token;
    }  
    
    
    
    public PasswordResetToken validateToken(String token){

        PasswordResetToken resetToken = pwdResetTokenRepo.findByToken(token)
        .orElseThrow(()-> new InvalidTokenException("Token not found"));

        if(resetToken.isExpired()){
            throw new InvalidTokenException("Token is expired");
        }

        if(resetToken.isUsed()){
            throw new InvalidTokenException("Token already used");
        }

        return resetToken;
    }
}