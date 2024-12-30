package com.sni.hairsalon.exception;

public class ResourceAlreadyExistException extends RuntimeException {
    private static final long serialLongUID = 3L;

    public ResourceAlreadyExistException(String message){
        super(message);
    }

    public ResourceAlreadyExistException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exist with %s : '%s'", resourceName, fieldName, fieldValue));
    }
    
    public ResourceAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
