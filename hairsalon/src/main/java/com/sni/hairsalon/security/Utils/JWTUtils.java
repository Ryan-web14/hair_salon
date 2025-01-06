package com.sni.hairsalon.security.Utils;

import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sni.hairsalon.dto.request.UserRequestDTO;
import com.sni.hairsalon.dto.response.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

@Component
public class JWTUtils {

    @Value("${jwt_secret}")
    private String secret;

    @Value("${jwt_expiration}")
    private long jwt_expiration;

    public String generateToken(UserResponseDTO user) throws IllegalArgumentException, JWTCreationException{
        Map<String,String> claims = new HashMap<>();
        claims.put("role", user.getRole());

        return JWT.create()
            .withClaim("user info", claims)
            .withSubject( user.getEmail())
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(jwt_expiration + System.currentTimeMillis()))
            .sign(Algorithm.HMAC256(secret));
    }


    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
        .build();
        DecodedJWT jwt =verifier.verify(token);
        return jwt.getSubject();
    }
}
