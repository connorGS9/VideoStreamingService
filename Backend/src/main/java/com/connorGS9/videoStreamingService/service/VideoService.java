package com.connorGS9.videoStreamingService.service;

import com.connorGS9.videoStreamingService.dto.PlaybackResponse;
import com.connorGS9.videoStreamingService.dto.VideoUploadRequest;
import com.connorGS9.videoStreamingService.exception.StorageException;
import com.connorGS9.videoStreamingService.exception.VideoNotFound;
import com.connorGS9.videoStreamingService.model.Video;
import com.connorGS9.videoStreamingService.model.VideoStatus;
import com.connorGS9.videoStreamingService.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final StorageService_S3_Wrapper s3Wrapper;

    // Inject through constructor
    public VideoService(VideoRepository repository, StorageService_S3_Wrapper s3Wrapper) {
        this.videoRepository = repository;
        this.s3Wrapper = s3Wrapper;
    }

    public Video getVideoById(Long id) {
        return videoRepository.findById(id).orElseThrow(() -> new VideoNotFound("Video with id: " + id + " could not be found"));
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public Video updateVideoStatus(Long id, VideoStatus newStatus) {
        Video vid = getVideoById(id);
        if (vid == null) {
            throw new VideoNotFound("Video with id: " + id + " does not exist");
        }
        vid.setStatus(newStatus);

        if (newStatus == VideoStatus.READY) {
            vid.setProcessedAt(LocalDateTime.now());
        }
        return videoRepository.save(vid);
    }

    public void deleteVideo(Long id) {
        if (!videoRepository.existsById(id)) {
            throw new VideoNotFound("Cannot delete, video not found with id: " + id);
        }
        videoRepository.deleteById(id);
    }

    // Method to upload the video by calling S3 wrapper and save the entry in the database
    public Video uploadVideo(MultipartFile file, VideoUploadRequest request) {
        // Validate inputs
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Create the video
        Video video = new Video(request.getTitle(), request.getDescription(), request.getUserId(), file.getOriginalFilename());
        // Save and get incremented id from database
        video = videoRepository.save(video);
        try {
            // Get the storagekey from the S3 wrapper using the file and videoId
            String storageKey = s3Wrapper.uploadRawVideo(file, video.getId());
            // Set the storageKey
            video.setStorageKey(storageKey);
            // Return the video and save in the DB
            return videoRepository.save(video);
        } catch (StorageException e) {
            // If the S3 storge fails, mark the video status as failed and re-save
            video.setStatus(VideoStatus.FAILED);
            videoRepository.save(video);
            throw e;
        }
    }

    public PlaybackResponse generatePlaybackURL(Long videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new VideoNotFound("Video with id: " + videoId + " does not exist"));

        if (!video.getStatus().equals(VideoStatus.READY)) {
            throw new RuntimeException("Video not ready for playback, current status is: " + video.getStatus());
        }

        String playlistKey = "processed/" + videoId + "/playlist.m3u8";
        String playbackURL = s3Wrapper.generatePresignedURL(playlistKey, Duration.ofHours(1));

        return new PlaybackResponse(playbackURL, video.getId(), video.getTitle(), video.getLengthSeconds());
    }


}
