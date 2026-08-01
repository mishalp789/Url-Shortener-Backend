package com.mishalp789.url_shortener.common.exception;

import com.mishalp789.url_shortener.common.response.ApiResponse;
import com.mishalp789.url_shortener.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex){

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex){

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex){

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(field ->
                        field.getField() + " : " + field.getDefaultMessage())
                .toList();

        ErrorResponse response = ErrorResponse.builder()
                .status(400)
                .error("Validation Failed")
                .message("Invalid Request")
                .details(errors)
                .build();

        return ResponseEntity.badRequest().body(response);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception ex){

        ErrorResponse response = ErrorResponse.builder()
                .status(500)
                .error("Internal Server Error")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.internalServerError().body(response);

    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUrlNotFound(
            UrlNotFoundException ex
    ){
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UrlExpiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleUrlExpired(UrlExpiredException ex){
        return ResponseEntity.status(HttpStatus.GONE)
                .body(
                        ApiResponse.<Void>builder()
                                .success(false)
                                .message(ex.getMessage())
                                .build()
                );
    }

}