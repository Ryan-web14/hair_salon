package com.sni.hairsalon.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
                request.getRequestURI(),
                getStackTrace(e)
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
                request.getRequestURI(),
                getStackTrace(e)
                );
            return new ResponseEntity<>(error, HttpStatus.PRECONDITION_FAILED);
        }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e,
    HttpServletRequest request ) {
        ErrorResponse error = ErrorResponse.of(
            e.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            "Invalid credential",
            request.getRequestURI(),
            getStackTrace(e));
            
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /*@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), errors);
    }*/


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequesException(
        BadRequestException e, HttpServletRequest request){

            ErrorResponse error = ErrorResponse.of(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad request",
                request.getRequestURI(),
                getStackTrace(e)
                );

            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
        Exception e, HttpServletRequest request){

            ErrorResponse error = ErrorResponse.of(
               "An unexpected error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Server side error occured",
                request.getRequestURI(),
                getStackTrace(e)
                );

            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        } 

        private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
