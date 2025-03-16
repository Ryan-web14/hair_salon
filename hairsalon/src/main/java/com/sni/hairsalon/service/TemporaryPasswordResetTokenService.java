package com.sni.hairsalon.service;

import com.sni.hairsalon.model.TemporaryPasswordResetToken;

public interface TemporaryPasswordResetTokenService {

    public String createToken(String email, String password);
    public TemporaryPasswordResetToken validateToken(String token);
    public void resetPassword(String token, String password);
    
}
