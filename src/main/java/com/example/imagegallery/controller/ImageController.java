package com.example.imagegallery.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.imagegallery.entity.ImageMetadata;
import com.example.imagegallery.exception.StorageException;
import com.example.imagegallery.service.ImageService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = {
 "http://localhost:4200",
 "http://angular-image-gallery-frontend.s3-website-us-east-1.amazonaws.com",
 "https://d2c19ha9yft0hf.cloudfront.net"
})
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;
	
	/**
     * Generates a pre-signed upload URL and stores metadata.
     * This allows the client to upload directly to S3.
     */
	@PostMapping("/upload-url")
	public ResponseEntity<ImageMetadata> generateUploadUrl(
			@RequestParam String fileName,
			@RequestParam String contentType,
			@RequestParam(required = false) Long size) {
		ImageMetadata imageMetadata = imageService.generatePresignedUpload(fileName, contentType, size);
		
		return ResponseEntity.ok(imageMetadata);
	}
	
	/**
     * Returns all uploaded image metadata.
     */
	@GetMapping()
	public ResponseEntity<List<ImageMetadata>> listImages() {
		return ResponseEntity.ok(imageService.getAllImages());
	}
	
	/**
     * Deletes an image from S3 and the database.
	 * @throws StorageException 
     */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteImage(@PathVariable Long id) throws StorageException {
		imageService.deleteImage(id);
		
		return ResponseEntity.noContent().build();
	}
}
