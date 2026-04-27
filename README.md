# FileUpload-SpringBoot

A Spring Boot REST API for managing file uploads, previews, and downloads. The application supports two storage mechanisms: local file system storage and an S3-compatible storage backend (RustFS).

## Technologies Used
* Java 21
* Spring Boot 4.0.6
* AWS SDK for Java (S3) 2.25.67
* RustFS (Dockerized S3-compatible storage)
* Springdoc OpenAPI (Swagger UI) 3.0.2
* Lombok
* Maven

## Architecture
* **v1 API (`/api/v1/files`)**: Handles file operations on the local file system (`src/main/resources/files`).
* **v2 API (`/api/v2/files`)**: Handles file operations using AWS S3 SDK to communicate with a local RustFS container.

## Prerequisites
* Java Development Kit (JDK) 21
* Docker and Docker Compose
* Maven

## Setup and Installation

### 1. Environment Configuration
Ensure a `.env` file exists in the root directory with the following variables for RustFS authentication:
```env
RUSTFS_ACCESS_KEY=yourStrongAccessKey123
RUSTFS_SECRET_KEY=yourSuperSecretKey456
```

### 2. Start RustFS Storage
Initialize the RustFS container using Docker Compose. The storage API will be available on port 9000, and the console on port 9001.
```bash
docker-compose up -d
```

### 3. Application Properties
The application utilizes the following default configuration in `application.properties`:
* Multipart max file size: 20MB
* Multipart max request size: 30MB
* RustFS URL: `http://localhost:9000`
* RustFS Bucket Name: `demo-file-upload` (Auto-created on first upload if it does not exist)

### 4. Run the Application
Use the Maven wrapper to build and run the Spring Boot application:
```bash
./mvnw spring-boot:run
```

## API Endpoints

### Local File System (v1)
* **Upload File**: `POST /api/v1/files/upload-file` (Consumes `multipart/form-data`)
* **Preview File**: `GET /api/v1/files/preview-file/{file-name}`
* **Download File**: `GET /api/v1/files/download-file/{file-name}`

### S3-Compatible Storage / RustFS (v2)
* **Upload File**: `POST /api/v2/files/upload-file` (Consumes `multipart/form-data`)
* **Preview File**: `GET /api/v2/files/preview-file/{file-name}`
* **Download File**: `GET /api/v2/files/download-file/{file-name}`
