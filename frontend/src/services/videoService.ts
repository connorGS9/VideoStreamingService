import axios from 'axios'
import type { Video } from '../types/Video'

const API_BASE_URL = 'http://localhost:8080/api';

export const videoService = {
    
    getAllVideos: async (): Promise<Video[]> => {
        const response = await axios.get(`${API_BASE_URL}/videos`);
        return response.data;
    },
    
    getVideoById: async (id: number): Promise<Video> => {
        const response = await axios.get(`${API_BASE_URL}/videos/${id}`);
        return response.data;
    },
    
    getPlaybackUrl: async (id: number): Promise<string> => {
        console.log('Fetching playback URL for video:', id);
        const response = await axios.get(`${API_BASE_URL}/videos/${id}/play`);
        console.log('Backend response:', response.data);
        console.log('Extracted playbackUrl:', response.data.playbackUrl);
        return response.data.playbackUrl;  // ← CRITICAL: Extract the playbackUrl field
    }
};