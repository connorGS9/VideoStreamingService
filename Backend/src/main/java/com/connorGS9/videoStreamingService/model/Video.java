package com.connorGS9.videoStreamingService.model;


import java.time.LocalDateTime;
import java.util.Objects;

public class Video {
    private Long id; // Unique id of video
    private String title; // Title of video
    private String description; // Description of video
    private Integer lengthSeconds; // In total seconds 2:24 = 144. Integer so it can be null
    private Long userId; // UserId of who uploaded this video
    private VideoStatus status; // Enum of uploaded, processing, ready, or failed
    private String storageKey; // Path to where the file is stored within Mini IO
    private LocalDateTime uploadedAt; // When was it uploaded?
    private LocalDateTime processedAt; // When did processing finish? (null if not done)
    private String originalFileName; // Original name of uploaded file
    private Integer width;   // 1920
    private Integer height;  // 1080
    private String codec;    // "h264"
    private Long fileSizeBytes; // File size in bytes

    public Video() {}

    public Video(String title, String description, Long userId, String originalFileName, VideoStatus status, LocalDateTime uploadedAt) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.originalFileName = originalFileName;
        this.status = status;
        this.uploadedAt = uploadedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLengthSeconds() {
        return lengthSeconds;
    }

    public void setLengthSeconds(Integer lengthSeconds) {
        this.lengthSeconds = lengthSeconds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    public VideoStatus getStatus() {
        return status;
    }

    public void setStatus(VideoStatus status) {
        this.status = status;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(Long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", lengthSeconds=" + lengthSeconds +
                ", userId=" + userId +
                ", status=" + status +
                ", storageKey='" + storageKey + '\'' +
                ", uploadedAt=" + uploadedAt +
                ", processedAt=" + processedAt +
                ", originalFileName='" + originalFileName + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", codec='" + codec + '\'' +
                ", fileSizeBytes=" + fileSizeBytes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(id, video.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
