package com.example.imagegallery.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	private LocalDateTime timestamp;
	private int status;
	private String error;
	private String messaje;
	private String path;
}
