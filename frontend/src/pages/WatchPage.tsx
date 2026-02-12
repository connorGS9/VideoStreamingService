import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import VideoPlayer from '../components/VideoPlayer';
import type { Video } from '../types/Video';
import { videoService } from '../services/videoService';

function WatchPage() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const [video, setVideo] = useState<Video | null>(null);
    const [playbackUrl, setPlaybackUrl] = useState<string>('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (id) {
            loadVideo(parseInt(id));
        }
    }, [id]);

    const loadVideo = async (videoId: number) => {
        try {
            const videoData = await videoService.getVideoById(videoId);
            setVideo(videoData);

            if (videoData.status === 'READY') {
                const url = await videoService.getPlaybackUrl(videoId);
                setPlaybackUrl(url);
            }
            
            setLoading(false);
        } catch (err) {
            console.error('Failed to load video:', err);
            setLoading(false);
        }
    };

    const formatDuration = (seconds: number | null): string => {
        if (!seconds) return '0:00';
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${mins}:${secs.toString().padStart(2, '0')}`;
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-darker flex items-center justify-center">
                <div className="text-gray-400">Loading video...</div>
            </div>
        );
    }

    if (!video) {
        return (
            <div className="min-h-screen bg-darker flex items-center justify-center">
                <div className="text-center">
                    <p className="text-gray-400 mb-4">Video not found</p>
                    <button
                        onClick={() => navigate('/')}
                        className="text-primary hover:underline"
                    >
                        ← Back to Home
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-primary">
            {/* Header */}
            <div className="bg-dark border-b border-gray-800">
                <div className="max-w-7xl mx-auto px-8 py-4">
                    <button
                        onClick={() => navigate('/')}
                        className="text-gray-400 hover:text-white transition flex items-center gap-2"
                    >
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
                        </svg>
                        Back to Home
                    </button>
                </div>
            </div>

            {/* Video Player */}
            <div className="max-w-7xl mx-auto px-8 py-8">
                {video.status === 'READY' && playbackUrl ? (
                    <VideoPlayer playbackUrl={playbackUrl} title={video.title} />
                ) : (
                    <div className="bg-dark rounded-lg p-12 text-center">
                        <p className="text-gray-400 mb-2">
                            Video is {video.status.toLowerCase()}
                        </p>
                        {video.status === 'PROCESSING' && (
                            <p className="text-sm text-gray-500">
                                This usually takes 10-30 seconds
                            </p>
                        )}
                        {video.status === 'UPLOADED' && (
                            <p className="text-sm text-gray-500">
                                Processing will begin shortly...
                            </p>
                        )}
                    </div>
                )}

                {/* Video Info */}
                <div className="mt-8 bg-dark rounded-lg p-6">
                    <h1 className="text-3xl font-bold mb-4">{video.title}</h1>
                    
                    <div className="flex gap-6 text-sm text-gray-400 mb-4">
                        <span>Duration: {formatDuration(video.lengthSeconds)}</span>
                        <span>•</span>
                        <span>Uploaded {new Date(video.uploadedAt).toLocaleDateString()}</span>
                        {video.width && video.height && (
                            <>
                                <span>•</span>
                                <span>{video.width}x{video.height}</span>
                            </>
                        )}
                    </div>

                    {video.description && (
                        <div className="mt-4">
                            <h2 className="font-semibold mb-2">Description</h2>
                            <p className="text-gray-300">{video.description}</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default WatchPage;