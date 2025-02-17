package com.sni.hairsalon.security.filter;

import com.sni.hairsalon.model.UserPrincipal;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sni.hairsalon.security.Utils.JWTUtils;
import com.sni.hairsalon.security.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final MyUserDetailsService userDetailsService;

    private final JWTUtils jwtUtils;
    
    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = extractToken(request);
            if (token != null) {
                processToken(token);
            }
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            logger.error("JWT Verification failed", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
        } catch (Exception e) {
            logger.error("Error processing JWT token", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing JWT token");
        }
    }

    private String extractToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith(BEARER_PREFIX)){
            String token = authHeader.substring(BEARER_PREFIX.length());
            if(token == null || token.isBlank()){
                logger.warn(("Empty jwt token found in header"));
               return null;
            }
            return token;
        }
        return null;
    }

    private void processToken(String token){
        String email = jwtUtils.validateTokenAndRetrieveSubject(token);
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserPrincipal userDetails = (UserPrincipal) userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Authentication successful for user: {}", email);
        }
    }

}
