package com.mishal.urlshortener.common;


import java.time.LocalDateTime;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleUrlNotFound(
        UrlNotFoundException ex,
        HttpServletRequest request
    ){
        log.error("Exception occured: {}",ex.getMessage(),ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of(
                "timestamp",LocalDateTime.now(),
                "status",404,
                "error","Not Found",
                "message",ex.getMessage(),
                "path",request.getRequestURI()
            ));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidationErrors(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ){
        String errorMessage = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .findFirst()
                                .map(error -> error.getField()+": "+error.getDefaultMessage())
                                .orElse("Validation error");
        log.error("Exception occured: {}",ex.getMessage(),ex);
        return ResponseEntity.badRequest().body(
            Map.of(
                "timestamp",LocalDateTime.now(),
                "status",400,
                "error","Bad Request",
                "message",errorMessage,
                "path",request.getRequestURI()
            )
        );

    }

    @ExceptionHandler(ShortCodeCollisionException.class)
    public ResponseEntity<Map<String,Object>> handleCollision(
        ShortCodeCollisionException ex,
        HttpServletRequest request
    ){
        log.error("Exception occured: {}",ex.getMessage(),ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                    "timestamp",LocalDateTime.now(),
                    "status",409,
                    "error", "Conflict",
                    "message", ex.getMessage(),
                    "path", request.getRequestURI()
                ));
    }

    @ExceptionHandler(UrlExpiredException.class)
    public ResponseEntity<Map<String,Object>> handleExpired(
        UrlExpiredException ex,
        HttpServletRequest request
    ){
        log.error("Exception occured: {}",ex.getMessage(),ex);
        return ResponseEntity.status(HttpStatus.GONE)
                .body(Map.of(
                    "timestamp", LocalDateTime.now(),
                    "status", 410,
                    "error", "Gone",
                    "message", ex.getMessage(),
                    "path", request.getRequestURI()
                ));
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<?> handleUsernameExists(UsernameAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Username already exists"
                ));
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleUsernameExists(EmailAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Email already exists"
                ));
    }

}
