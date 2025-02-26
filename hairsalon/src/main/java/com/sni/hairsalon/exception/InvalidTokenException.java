package com.sni.hairsalon.exception;

public class InvalidTokenException extends RuntimeException {
     
    private static final long serialLongUID = 4L;

    public InvalidTokenException(String message){
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause){
        super(message, cause);
    }
}
