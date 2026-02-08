package com.connorGS9.videoStreamingService.service;

import com.connorGS9.videoStreamingService.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

@Service
public class StorageService_S3_Wrapper {
    //Upload raw video(), downloadFile(), uploadProcessedVideo(), deleteFile(), generatePresignedUrl(), private getFileExtension(), private buildS3Key()
    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public StorageService_S3_Wrapper(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadRawVideo(MultipartFile file, Long videoId) throws StorageException {
        if (file == null || file.isEmpty()) {
            throw new StorageException("File cannot be null or empty");
        }
        if (videoId == null) {
            throw new StorageException("Video ID cannot be null");
        }

        String key = "/raw/" + videoId + "/original." + getFileExtension(file.getOriginalFilename());
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

}
