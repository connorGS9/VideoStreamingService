package com.connorGS9.videoStreamingService.dto;

public class PlaybackResponse {
    private String playbackUrl;
    private Long videoId;
    private String title;
    private Integer durationSeconds;

    public PlaybackResponse() {
    }

    public PlaybackResponse(String playbackUrl, Long videoId, String title, Integer durationSeconds) {
        this.playbackUrl = playbackUrl;
        this.videoId = videoId;
        this.title = title;
        this.durationSeconds = durationSeconds;
    }

    public String getPlaybackUrl() {
        return playbackUrl;
    }

    public void setPlaybackUrl(String playbackUrl) {
        this.playbackUrl = playbackUrl;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
