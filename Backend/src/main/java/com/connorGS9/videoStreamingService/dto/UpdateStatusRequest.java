package com.connorGS9.videoStreamingService.dto;

import com.connorGS9.videoStreamingService.model.VideoStatus;

public class UpdateStatusRequest {
    private VideoStatus status;

    public VideoStatus getStatus() {
        return status;
    }

    public void setStatus(VideoStatus status) {
        this.status = status;
    }
}
