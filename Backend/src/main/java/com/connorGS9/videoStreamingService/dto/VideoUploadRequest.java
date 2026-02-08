package com.connorGS9.videoStreamingService.dto;


public class VideoUploadRequest {
    private String title;
    private String description;
    private Long userId;

    public VideoUploadRequest() {
    }

    public VideoUploadRequest(String title, String description, Long userId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
