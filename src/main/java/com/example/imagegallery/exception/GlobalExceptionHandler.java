package com.example.imagegallery.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoudException(ResourceNotFoundException ex, HttpServletRequest request) {
		ErrorResponse error = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.error("Resource Not Found")
				.messaje(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(StorageException.class)
	public ResponseEntity<ErrorResponse> handleStorageException(StorageException ex, HttpServletRequest request) {
		ErrorResponse error = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_GATEWAY.value())
				.error("S3 Error")
				.messaje(ex.getMessage())
				.path(request.getRequestURI())
				.build();
		
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
	}
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
				.timestamp(LocalDateTime.now())
				.status( HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error("Internal Server Error")
				.messaje(ex.getMessage())
				.path(request.getRequestURI())
				.build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
