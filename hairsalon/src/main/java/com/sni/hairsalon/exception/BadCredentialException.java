package com.sni.hairsalon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadCredentialException extends RuntimeException {
    private static final long serialLongUID = 2L;

    public BadCredentialException(String message){
        super(message);
    }

    public BadCredentialException(String message, Throwable e){
        super(message, e);
    }
}

