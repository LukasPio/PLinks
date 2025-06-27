package com.lucas.plinks.exception;

import com.lucas.plinks.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.toString(),
                "invalid URL was provided",
                null,
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidUrlException(InvalidUrlException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.toString(),
                "invalid URL was provided",
                null,
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
