package com.connorGS9.videoStreamingService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VideoStreamingServiceApplication {
    // Entry point
    public static void main(String[] args) {
        SpringApplication.run(VideoStreamingServiceApplication.class, args);
    }


}
