package com.connorGS9.videoStreamingService.service;

import com.connorGS9.videoStreamingService.dto.VideoMetadata;
import com.connorGS9.videoStreamingService.exception.FFmpegException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FFmpegService {

    public void transcodeToHLS(File inputFile, File outputDir) throws FFmpegException {
        if (!inputFile.exists()) {
            throw new FFmpegException("Input file does not exist");
        }
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        ProcessBuilder processBuilder = buildFFmpegCommand(inputFile, outputDir);

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                String errorStream = readStream(process.getErrorStream());
                throw new FFmpegException("FFmpeg failed: " + errorStream);
            }

        } catch (IOException | InterruptedException e) {
            throw new FFmpegException("Failed to run FFmpeg", e);
        }
    }

    public VideoMetadata extractMetadata(File videoFile) throws FFmpegException{
        if (!videoFile.exists()) {
            throw new FFmpegException("Video file does not exist.");
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffprobe",
                    "-v", "quiet",
                    "-print_format", "json",
                    "-show_format",
                    "-show_streams",
                    videoFile.getAbsolutePath()
            );
            Process process = processBuilder.start();
            String jsonOutput = readStream(process.getInputStream());
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new FFmpegException("FFprobe failed with exit code: " + exitCode);
            }
            return parseMetadataFromJson(jsonOutput);

        } catch (IOException | InterruptedException e) {
            throw new FFmpegException("Failed to extract metadata from video file.");
        }
    }

    private ProcessBuilder buildFFmpegCommand(File inputFile, File outputDir) {
        String outputPath = outputDir.getAbsolutePath();
        List<String> command = new ArrayList<>();

        command.add("ffmpeg");
        command.add("-i");
        command.add(inputFile.getAbsolutePath());

        // Video codec
        command.add("-c:v");
        command.add("libx264");

        // Audio codec
        command.add("-c:a");
        command.add("aac");

        // Video bitrate (quality)
        command.add("-b:v");
        command.add("3000k");

        // Resolution (720p)
        command.add("-s");
        command.add("1280x720");

        // HLS settings
        command.add("-hls_time");
        command.add("4");  // 4 second segments

        command.add("-hls_list_size");
        command.add("0");  // Include all segments

        // Segment filename pattern
        command.add("-hls_segment_filename");
        command.add(outputPath + File.separator + "segment%03d.ts");

        // Output playlist
        command.add(outputPath + File.separator + "playlist.m3u8");

        return new ProcessBuilder(command);
    }

    private String readStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder errorOutput = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }

        return errorOutput.toString();
    }

    private VideoMetadata parseMetadataFromJson(String json) throws FFmpegException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            // Get first stream
            JsonNode streams = root.get("streams");
            JsonNode videoStream = null;

            for (JsonNode stream : streams) {
                if ("video".equals(stream.get("codec_type").asText())) {
                    videoStream = stream;
                    break;
                }
            }

            // Extract metadata
            Integer width = videoStream != null ? videoStream.get("width").asInt() : null;
            Integer height = videoStream != null ? videoStream.get("height").asInt() : null;
            String codec = videoStream != null ? videoStream.get("codec_name").asText() : null;

            JsonNode format = root.get("format");
            Double duration = format.has("duration") ? format.get("duration").asDouble() : null;
            Long fileSize = format.has("size") ? format.get("size").asLong() : null;
            Integer durationSeconds = duration != null ? duration.intValue() : null;

            return new VideoMetadata(durationSeconds, width, height, codec, fileSize);
        } catch(Exception e) {
            throw new FFmpegException("Failed to parse Json metadata.");
        }
    }
}
