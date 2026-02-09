package com.connorGS9.videoStreamingService.worker;

import com.connorGS9.videoStreamingService.dto.VideoMetadata;
import com.connorGS9.videoStreamingService.model.Video;
import com.connorGS9.videoStreamingService.model.VideoStatus;
import com.connorGS9.videoStreamingService.repository.VideoRepository;
import com.connorGS9.videoStreamingService.service.FFmpegService;
import com.connorGS9.videoStreamingService.service.StorageService_S3_Wrapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VideoProcessingWorker {
    // Runs in the background every 10 seconds, finds videos with status `UPLOADED`, and processes them.
    private final VideoRepository videoRepository;
    private final StorageService_S3_Wrapper storageService;
    private final FFmpegService ffmpegService;


    public VideoProcessingWorker(VideoRepository videoRepository, StorageService_S3_Wrapper storageService, FFmpegService ffmpegService) {
        this.videoRepository = videoRepository;
        this.storageService = storageService;
        this.ffmpegService = ffmpegService;
        System.out.println("VideoProcessingWorker initialized - will check for videos every 10 seconds");
    }

    @Scheduled(fixedDelay = 10000)
    public void processVideos() {
        System.out.println("Checking for videos to process...");
        List<Video> videosToProcess = videoRepository.findByStatus(VideoStatus.UPLOADED);
        if (videosToProcess.isEmpty()) {
            System.out.println("No videos to process");
            return;  // Nothing to process
        }

        for (Video video : videosToProcess) {
            try {
                processVideo(video);
            } catch (Exception e) {
                // Mark as failed if processing fails
                System.err.println("Failed to process video " + video.getId() + ": " + e.getMessage());
                video.setStatus(VideoStatus.FAILED);
                videoRepository.save(video);
            }
        }
    }

    private void processVideo(Video video) throws IOException {
        // Download from S3
        // Transcode with FFmpeg
        // Upload to S3
        // Update status
        System.out.println("Processing video: " + video.getId());

        // 1. Create temp directories
        File tempDir = Files.createTempDirectory("video-processing-").toFile();
        File rawVideoFile = new File(tempDir, "input.mp4");
        File outputDir = new File(tempDir, "hls-output");
        outputDir.mkdirs();

        try {
            // 2. Download raw video from S3
            System.out.println("Downloading from S3: " + video.getStorageKey());
            storageService.downloadFile(video.getStorageKey(), rawVideoFile);

            // 3. Extract metadata
            VideoMetadata metadata = ffmpegService.extractMetadata(rawVideoFile);
            video.setLengthSeconds(metadata.getDurationSeconds());
            video.setWidth(metadata.getWidth());
            video.setHeight(metadata.getHeight());
            video.setCodec(metadata.getCodec());
            video.setFileSizeBytes(metadata.getFileSizeBytes());
            videoRepository.save(video);

            // 4. Transcode to HLS
            System.out.println("Transcoding to HLS...");
            ffmpegService.transcodeToHLS(rawVideoFile, outputDir);

            // 5. Upload HLS files to S3
            String s3Prefix = "processed/" + video.getId() + "/";
            System.out.println("Uploading to S3: " + s3Prefix);
            storageService.uploadDirectory(outputDir, s3Prefix);

            // 6. Update video status
            video.setStatus(VideoStatus.READY);
            video.setProcessedAt(LocalDateTime.now());
            videoRepository.save(video);

            System.out.println("Video " + video.getId() + " processed successfully!");

        } finally {
            // 7. Clean up temp files
            deleteDirectory(tempDir);
        }
    }

    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}
