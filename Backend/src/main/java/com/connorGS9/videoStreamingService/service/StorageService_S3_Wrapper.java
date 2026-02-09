package com.connorGS9.videoStreamingService.service;

import com.connorGS9.videoStreamingService.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

@Service
public class StorageService_S3_Wrapper {
    //Upload raw video(), downloadFile(), uploadProcessedVideo(), deleteFile(), generatePresignedUrl(), private getFileExtension(), private buildS3Key()
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public StorageService_S3_Wrapper(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    public String uploadRawVideo(MultipartFile file, Long videoId) throws StorageException {
        if (file == null || file.isEmpty()) {
            throw new StorageException("File cannot be null or empty");
        }
        if (videoId == null) {
            throw new StorageException("Video ID cannot be null");
        }

        String key = "raw/" + videoId + "/original." + getFileExtension(file.getOriginalFilename());
        try {
            s3Client.putObject(builder ->
                    builder.bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType())
                            , RequestBody.fromInputStream(file.getInputStream(), file.getSize())
                    );
        } catch (IOException e) {
            throw new StorageException("Failed to upload file to S3 bucket: " + key, e);
        }
        return key;
    }

    // Passes original filename in order to grab the extension and return it
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("File name can not be empty / null.");
        }
        int lastDot = filename.lastIndexOf('.'); // Everything after should be the extension :: mp4, avi, etc.

        if (lastDot == -1 || lastDot == filename.length() - 1) {
            throw new IllegalArgumentException("Filename must have a valid extension");
        }
        return filename.substring(lastDot + 1).toLowerCase(); // Take everything after the last dot and transform to lower case if needed
    }

    public void downloadFile(String key, File destinationFile) throws StorageException{
        try {
            s3Client.getObject(
                    request -> request
                            .bucket(bucketName)
                            .key(key),
                            destinationFile.toPath()
            );
        } catch (Exception e) {
            throw new StorageException("Could not download the file at storage key: " + key, e);
        }
    }

    public void uploadDirectory(File directory, String S3Prefix) throws StorageException{
        if (!directory.exists() || !directory.isDirectory()) {
            throw new StorageException("Invalid directory: " + directory);
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // Recursively enter the directory until we get individual files to upload
                uploadDirectory(file, S3Prefix + file.getName() + "/");
            } else {
                String key = S3Prefix + file.getName();
                uploadFile(file, key);
            }
        }
    }

    private void uploadFile(File file, String key) {
        try {
            s3Client.putObject(
                    request -> request
                            .bucket(bucketName)
                            .key(key)
                            .contentType(getContentType(file.getName())),
                            file.toPath()
            );
        } catch (Exception e) {
            throw new StorageException("Failed to upload file to S3: " + key, e);
        }
    }

    private String getContentType(String filename) {
        if (filename.endsWith(".m3u8")) {
            return "application/vnd.apple.mpegurl";
        } else if (filename.endsWith(".ts")) {
            return "video/mp2t";
        }
        return "application/octet-stream";
    }

    public String generatePresignedURL(String key, Duration expiration) throws StorageException{
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(expiration)
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

            return presignedRequest.url().toString();
        } catch(Exception e) {
            throw new StorageException("Failed to generate presigned URL for key: " + key, e);
        }
    }
}
