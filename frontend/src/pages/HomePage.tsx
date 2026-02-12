import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import type { Video } from '../types/Video';
import { videoService } from '../services/videoService';
import VideoCard from '../components/Videocard';

function HomePage() {
    const [videos, setVideos] = useState<Video[]>([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        fetchVideos();
    }, []);

    const fetchVideos = async () => {
        try {
            const data = await videoService.getAllVideos();
            // Only show READY videos on home page
            setVideos(data.filter(v => v.status === 'READY'));
            setLoading(false);
        } catch (err) {
            console.error('Failed to fetch videos:', err);
            setLoading(false);
        }
    };

    return (
      <div className="min-h-screen bg-dark">
            {/* Hero Section */}
            <div className="bg-gradient-to-r from-primary to-secondary py-20 px-8">
                <div className="max-w-7xl mx-auto">
                    <h1 className="text-5xl font-bold text-white mb-4">
                        Connor's Video Streaming Service
                    </h1>
                    <p className="text-xl text-gray-200 mb-8">
                        Upload, process, and stream videos with HLS technology
                    </p>
                    <button
                        onClick={() => navigate('/upload')}
                        className="bg-white text-primary px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 transition"
                    >
                        Upload Video
                    </button>
                </div>
            </div>

            {/* Video Grid */}
            <div className="max-w-7xl mx-auto px-8 py-12">
                <h2 className="text-3xl font-bold mb-8">Available Videos</h2>
                
                {loading && (
                    <div className="text-center py-12">
                        <div className="text-gray-400">Loading videos...</div>
                    </div>
                )}

                {!loading && videos.length === 0 && (
                    <div className="text-center py-12">
                        <p className="text-gray-400 mb-4">No videos yet</p>
                        <button
                            onClick={() => navigate('/upload')}
                            className="text-primary hover:underline"
                        >
                            Upload your first video →
                        </button>
                    </div>
                )}

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                    {videos.map(video => (
                        <VideoCard
                            key={video.id}
                            video={video}
                            onClick={() => navigate(`/watch/${video.id}`)}
                        />
                    ))}
                </div>
            </div>
        </div>
    );
}

export default HomePage;