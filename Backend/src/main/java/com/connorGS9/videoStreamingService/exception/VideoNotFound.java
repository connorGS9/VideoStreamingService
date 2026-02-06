package com.connorGS9.videoStreamingService.exception;

public class VideoNotFound extends RuntimeException {
    public VideoNotFound(String message) {
        super(message);
    }
}
