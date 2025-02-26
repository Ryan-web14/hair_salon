package com.sni.hairsalon.service;

import com.sni.hairsalon.model.PasswordResetToken;

public interface PasswordResetTokenService {
    
    public String createToken(String email);
    public PasswordResetToken validateToken(String token);
    
}
