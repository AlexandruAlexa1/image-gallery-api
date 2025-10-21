package com.example.imagegallery.service;

import java.net.URL;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.imagegallery.exception.StorageException;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Client s3Client;
	private final S3Presigner s3Presigner;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;
	
	/**
	 * Generates a temporary (pre-signed) URL for direct upload to AWS S3.
	 * 
	 * <p>The flow is as follows:
	 * 1. Creates a unique file name (key) using UUID.
	 * 2. Builds a {@link PutObjectRequest} with:
	 *    - the S3 bucket,
	 *    - the generated key,
	 *    - the file's content type,
	 *    - public-read permission (can be modified if the file should be private).
	 * 3. Creates a {@link PutObjectPresignRequest} with an expiration duration (e.g., 5 minutes).
	 * 4. Uses {@link S3Presigner} to generate the signed URL.
	 * 
	 * @param fileName Original name of the uploaded file
	 * @param contentType MIME type of the file (e.g., "image/png")
	 * @return Temporary signed URL that the client can use for direct upload to S3
	 */
	public URL generateUploadUrl(String key, String contentType) {
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
			.bucket(bucketName)
			.key(key)
			.contentType(contentType)
			.build();
		
		PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
			.signatureDuration(Duration.ofMinutes(5))
			.putObjectRequest(putObjectRequest)
			.build();
		
		PresignedPutObjectRequest presignedObject = s3Presigner.presignPutObject(presignRequest);
		
		return presignedObject.url();
	}
	
	public void deleteFile(String key) throws StorageException {
		try {
			s3Client.deleteObject(DeleteObjectRequest.builder()
					.bucket(bucketName)
					.key(key)
					.build()
			);
		} catch (S3Exception e) {
			String errorCode = e.awsErrorDetails().errorCode();
			
			switch (errorCode) {
	            case "NoSuchBucket" -> throw new StorageException("Bucket not found: " + bucketName, e);
	            case "NoSuchKey" -> throw new StorageException("File not found in S3: " + key, e);
	            case "AccessDenied" -> throw new StorageException("Access denied to bucket " + bucketName, e);
	            default -> throw new StorageException("Unexpected S3 error: " + e.awsErrorDetails().errorMessage(), e);
	        }
		} 
	}
}
