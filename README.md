# Image Gallery API

![Build and Deploy](https://github.com/AlexandruAlexa1/image-gallery-api/actions/workflows/build-deploy.yml/badge.svg?branch=main)
![Java Version](https://img.shields.io/badge/java-17-blue)
![License](https://img.shields.io/github/license/AlexandruAlexa1/image-gallery-api)
![Repo Size](https://img.shields.io/github/repo-size/AlexandruAlexa1/image-gallery-api)
![Last Commit](https://img.shields.io/github/last-commit/AlexandruAlexa1/image-gallery-api)
![Issues](https://img.shields.io/github/issues/AlexandruAlexa1/image-gallery-api)
![Forks](https://img.shields.io/github/forks/AlexandruAlexa1/image-gallery-api)
![Stars](https://img.shields.io/github/stars/AlexandruAlexa1/image-gallery-api)

A Spring Boot REST API to upload, list, and delete images, integrated with **AWS S3** and **RDS MySQL**. Demonstrates cloud-ready architecture and scalable design.

---

## Features

- Upload images directly to **S3** using pre-signed URLs.
- Store metadata in **MySQL** (`fileName`, `url`, `key`, `contentType`, `size`, `uploadedAt`).
- List all images.
- Delete images from **S3** and metadata from DB.
- Health monitoring via Spring Actuator (`/actuator/health`).

---

## Database Configuration

The application uses different databases depending on the active Spring profile:

| Profile | Database | Description |
|---------|----------|-------------|
| `dev`   | H2 (in-memory) | Local development and testing without cloud costs. |
| `prod`  | AWS RDS MySQL | Persistent metadata storage in production. |

---

## API Endpoints

| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/api/images/upload-url` | Generate pre-signed URL for upload |
| GET  | `/api/images` | List all images |
| DELETE | `/api/images/{id}` | Delete an image |

---

## AWS Setup

- **S3 bucket** for storing images.
- **RDS MySQL** for metadata.
- **Elastic Beanstalk** for deployment.
- Configure environment variables for DB and bucket.
- Bucket policy allows `s3:GetObject` for public or controlled access.

---

## Quick Example

Generate pre-signed URL:

```bash
POST /api/images/upload-url?fileName=test.png&contentType=image/png
{
  "id": 1,
  "fileName": "test.png",
  "key": "images/uuid_test.png",
  "url": "https://bucket.s3.amazonaws.com/images/uuid_test.png",
  "contentType": "image/png",
  "size": 102400,
  "uploadedAt": "2025-10-16T10:00:00"
}
```
---

## Skills

- Spring Boot + JPA
- AWS S3 + RDS
- Pre-signed URLs for secure file upload
- CI/CD with GitHub Actions
- Cloud deployment and scalable architecture
- CI/CD with GitHub Actions (build, test, deploy)
- Automated HTML test reporting ([View Test Report](https://AlexandruAlexa1.github.io/image-gallery-api/))

---

## Frontend Repository

The Angular frontend for this project can be found [here](https://github.com/AlexandruAlexa1/image-gallery-frontend).
