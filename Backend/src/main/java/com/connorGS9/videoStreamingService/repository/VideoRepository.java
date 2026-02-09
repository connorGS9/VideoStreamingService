package com.connorGS9.videoStreamingService.repository;

import com.connorGS9.videoStreamingService.model.VideoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.connorGS9.videoStreamingService.model.Video;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByUserId(Long userId);

    List<Video> findByStatus(VideoStatus videoStatus);
}
