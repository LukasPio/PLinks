package com.lucas.plinks.exception;

import com.lucas.plinks.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidLinkException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidLinkException() {
        ApiResponse<Void> response = new ApiResponse<>("Invalid link was provided", new Timestamp(System.currentTimeMillis()), HttpStatus.BAD_REQUEST.value(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(SlugAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleSlugAlreadyExistsException() {
        ApiResponse<Void> response = new ApiResponse<>("Slug already was registered", new Timestamp(System.currentTimeMillis()), HttpStatus.CONFLICT.value(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(SlugNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleSlugNotFoundException() {
        ApiResponse<Void> response = new ApiResponse<>("Slug was not found", new Timestamp(System.currentTimeMillis()), HttpStatus.NOT_FOUND.value(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
