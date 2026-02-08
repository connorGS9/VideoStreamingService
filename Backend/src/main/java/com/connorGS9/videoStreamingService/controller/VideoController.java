package com.connorGS9.videoStreamingService.controller;

import com.connorGS9.videoStreamingService.dto.UpdateStatusRequest;
import com.connorGS9.videoStreamingService.dto.VideoCreateRequest;
import com.connorGS9.videoStreamingService.dto.VideoUploadRequest;
import com.connorGS9.videoStreamingService.model.Video;
import com.connorGS9.videoStreamingService.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
    public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService service) { //Initialize VideoService through constructor injection
        this.videoService = service;
    }

    //CRUD endpoints :: POST create, get specific video, get all videos, update video status, delete video

    //when a POST request is sent to /api/videos/upload, create the video object and
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public Video uploadVideo(@RequestParam("file") MultipartFile file, @ModelAttribute VideoUploadRequest request) {
        return videoService.uploadVideo(file, request);
    }

    // When get request is sent to /api/videos/# the video with id '#' will be returned
    @GetMapping("/{id}")
    public Video getVideo(@PathVariable Long id) {
        return videoService.getVideoById(id);
    }

    // When get request is sent to /api/videos, an arraylist of all videos will be returned
    @GetMapping
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    // When PUT request is sent to /api/videos/#/status the video with id '#' will have its status updated to one of 4 videoStatus enum values
    @PutMapping("/{id}/status")
    public Video updateStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        return videoService.updateVideoStatus(id, request.getStatus());
    }

    // When a DELETE request is sent to api/videos/#, video with unique id '#' will be deleted
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
    }

}
