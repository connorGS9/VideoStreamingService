package com.connorGS9.videoStreamingService.exception;

public class FFmpegException extends RuntimeException {
    public FFmpegException(String message) {
        super(message);
    }

    public FFmpegException(String message, Throwable cause) {
        super(message, cause);
    }
}
