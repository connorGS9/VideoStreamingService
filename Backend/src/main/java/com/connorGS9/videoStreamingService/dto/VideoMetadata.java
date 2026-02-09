package com.connorGS9.videoStreamingService.dto;

public class VideoMetadata {
    private Integer durationSeconds;
    private Integer width;
    private Integer height;
    private String codec;
    private Long fileSizeBytes;

    public VideoMetadata() {}

    public VideoMetadata(Integer durationSeconds, Integer width, Integer height,
                         String codec, Long fileSizeBytes) {
        this.durationSeconds = durationSeconds;
        this.width = width;
        this.height = height;
        this.codec = codec;
        this.fileSizeBytes = fileSizeBytes;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
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
}
