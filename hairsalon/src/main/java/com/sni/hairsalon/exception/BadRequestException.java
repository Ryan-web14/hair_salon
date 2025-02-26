package com.sni.hairsalon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private static final long serialLongUID = 2L;

    public BadRequestException(String message){
        super(message);
    }

    public BadRequestException(String message, Throwable e){
        super(message, e);
    }
}
 