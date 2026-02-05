package com.connorGS9.videoStreamingService.model;


import java.time.LocalDateTime;

public class Video {
    private Long id; // Unique id of video
    private String title; // Title of video
    private String description; // Description of video
    private Integer lengthSeconds; // In total seconds 2:24 = 144. Integer so it can be null
    private Long userId; // UserId of who uploaded this video
    private VideoStatus staus; // Enum of uploaded, processing, ready, or failed
    private String storageKey; // Path to where the file is stored within Mini IO
    private LocalDateTime uploadedAt; // When was it uploaded?
    private LocalDateTime processedAt; // When did processing finish? (null if not done)
    private String originalFileName; // Original name of uploaded file
    private Integer width;   // 1920
    private Integer height;  // 1080
    private String codec;    // "h264"
    private Long fileSizeBytes; // File size in bytes
}
