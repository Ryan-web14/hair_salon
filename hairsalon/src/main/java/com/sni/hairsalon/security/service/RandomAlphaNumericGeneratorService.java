package com.sni.hairsalon.security.service;

import java.security.SecureRandom;
import org.springframework.stereotype.Service;

@Service
public class RandomAlphaNumericGeneratorService {
    
    private static final String ALPHA_NUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    + "abcdefghijklmnopqrstuvwxyz"
    + "0123456789";

    private final SecureRandom random = new SecureRandom();

    public String generateRandomAlphaNumeric(int length){

        StringBuilder randomString = new StringBuilder(length);

        for(int i = 0; i <= length; i++){
            int randomIndex = random.nextInt(ALPHA_NUMERIC_CHARS.length());
            randomString.append(ALPHA_NUMERIC_CHARS.charAt(randomIndex));
        }
       
        return randomString.toString();
    }

}
