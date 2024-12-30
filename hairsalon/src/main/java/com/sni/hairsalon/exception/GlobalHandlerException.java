package com.sni.hairsalon.exception;

import org.eclipse.angus.mail.iap.ResponseInputStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalHandlerException {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException e, HttpServletRequest request){
            
            ErrorResponse error = ErrorResponse.of(
                e.getMessage(), 
                HttpStatus.NOT_FOUND.value(),
                "not found",
                request.getRequestURI()
                );

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ErrorResponse>handleResourceAlreadyException(
        ResourceAlreadyExistException e, HttpServletRequest request){
            
                ErrorResponse error = ErrorResponse.of(
                e.getMessage(), 
                HttpStatus.NOT_FOUND.value(),
                "already exist",
                request.getRequestURI()
                );
            return new ResponseEntity<>(error, HttpStatus.PRECONDITION_FAILED);
        }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequesException(
        BadRequestException e, HttpServletRequest request){

            ErrorResponse error = ErrorResponse.of(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad request",
                request.getRequestURI());

            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
        Exception e, HttpServletRequest request){

            ErrorResponse error = ErrorResponse.of(
               "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Server side error occured",
                request.getRequestURI());

            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        } 
}
