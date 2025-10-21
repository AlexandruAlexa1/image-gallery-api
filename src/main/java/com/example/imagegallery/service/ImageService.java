package com.example.imagegallery.service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.imagegallery.entity.ImageMetadata;
import com.example.imagegallery.exception.ResourceNotFoundException;
import com.example.imagegallery.exception.StorageException;
import com.example.imagegallery.repository.ImageMetadataRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final S3Service s3Service;
	private final ImageMetadataRepository imageMetadataRepository;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;
	
	/**
	 * Generates a pre-signed URL for direct upload to S3, saves the metadata in the database,
	 * and returns the metadata containing the temporary pre-signed URL for upload.
	 *
	 * @param fileName the original file name
	 * @param contentType the MIME type (e.g., image/png)
	 * @param size the file size (optional, can be sent by the client)
	 * @return ImageMetadata containing the pre-signed upload URL
	 */
	public ImageMetadata generatePresignedUpload(String fileName, String contentType, Long size) {
	    String prefix = "images/";
	    String key = prefix + UUID.randomUUID() + "_" + fileName;

	    URL uploadUrl = s3Service.generateUploadUrl(key, contentType);

	    String permanentUrl = "https://" + bucketName + ".s3.amazonaws.com/" + key;

	    ImageMetadata imageMetadata = ImageMetadata.builder()
	            .fileName(fileName)
	            .key(key)
	            .url(permanentUrl)
	            .contentType(contentType)
	            .size(size)
	            .uploadedAt(LocalDateTime.now())
	            .build();

	    imageMetadataRepository.save(imageMetadata);

	    imageMetadata.setUrl(uploadUrl.toString());
	    return imageMetadata;
	}

	
	public List<ImageMetadata> getAllImages() {
		return imageMetadataRepository.findAll();
	}
	
	public void deleteImage(Long id) throws StorageException {
		ImageMetadata imageMetadata = imageMetadataRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Image with ID " + id + " not found"));
		
		s3Service.deleteFile(imageMetadata.getKey());
		imageMetadataRepository.deleteById(id);
	}
}
