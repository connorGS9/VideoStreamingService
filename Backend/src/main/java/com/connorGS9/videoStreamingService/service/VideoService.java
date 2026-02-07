package com.connorGS9.videoStreamingService.service;

import com.connorGS9.videoStreamingService.exception.VideoNotFound;
import com.connorGS9.videoStreamingService.model.Video;
import com.connorGS9.videoStreamingService.model.VideoStatus;
import com.connorGS9.videoStreamingService.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private AtomicLong idCounter = new AtomicLong(1);

    // Inject through constructor
    public VideoService(VideoRepository repository) {this.videoRepository = repository;}

    public Video createVideo(String title, String description, Long userId, String filename) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        Video vid = new Video(title, description, userId, filename);
        return videoRepository.save(vid);
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
}
