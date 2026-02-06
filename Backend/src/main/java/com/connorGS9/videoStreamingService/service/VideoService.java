package com.connorGS9.videoStreamingService.service;

import com.connorGS9.videoStreamingService.exception.VideoNotFound;
import com.connorGS9.videoStreamingService.model.Video;
import com.connorGS9.videoStreamingService.model.VideoStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VideoService {

    private HashMap<Long, Video> videoStore = new HashMap<>();
    private AtomicLong idCounter = new AtomicLong(1);

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
        Long videoId = idCounter.getAndIncrement();
        vid.setId(videoId);
        videoStore.put(videoId, vid);
        return vid;
    }

    public Video getVideoById(Long id) {
        if (!videoStore.containsKey(id)) throw new VideoNotFound("Video with id:" + id + "does not exist");
        return videoStore.get(id);
    }

    public List<Video> getAllVideos() {
        return new ArrayList<>(videoStore.values());
    }

    public Video updateVideoStatus(Long id, VideoStatus newStatus) {
        Video vid = getVideoById(id);
        if (vid == null) {
            throw new VideoNotFound("Video with id: " + id + " does not exist");
        }
        vid.setStatus(newStatus);
        return vid;
    }

    public void deleteVideo(Long id) {
        if (!videoStore.containsKey(id)) {
            throw new VideoNotFound("Cannot delete - video not found with id: " + id);
        }
        videoStore.remove(id);
    }
}
