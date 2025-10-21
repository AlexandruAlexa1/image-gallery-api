package com.example.imagegallery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.imagegallery.entity.ImageMetadata;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, Long> {

}
