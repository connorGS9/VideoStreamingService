import { useEffect, useState } from 'react'
import './App.css'
import Videocard from './components/Videocard'
import type { Video } from './types/Video'
import { videoService } from './services/videoService'
import VideoUploadForm from './components/VideoUploadForm';
import VideoPlayer from './components/VideoPlayer';

function App() {
  // State = data that can change (explained below)
    const [videos, setVideos] = useState<Video[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string>('');

     // Player state
    const [currentVideo, setCurrentVideo] = useState<Video | null>(null);
    const [playbackUrl, setPlaybackUrl] = useState<string>('');

    // useEffect = runs when component loads
    useEffect(() => {
        fetchVideos();
    }, []); // Empty array = run once on mount

    const fetchVideos = async () => {
        try {
          setLoading(true);
          const data = await videoService.getAllVideos();
          setVideos(data);
          setError('');
        } catch (err) {
            console.error('Failed to fetch videos:', err);
            setError('Failed to load videos. Ensure backend is running on port 8080');
        } finally {
            setLoading(false);
      }
  };

  const handlePlayClick = async (videoId: number) => {
    try {
        // Find the video 
        const video = videos.find(v => v.id === videoId);
        if (!video) {
            return;
        }

        console.log('Getting playback URL for video:', videoId);
        
        // Get playback URL from backend
        const url = await videoService.getPlaybackUrl(videoId);
        
        console.log('URL received in handlePlayClick:', url);
        console.log('URL type:', typeof url);
        console.log('URL length:', url?.length);
        
        // CRITICAL: Set these in the right order
        setCurrentVideo(video);
        setPlaybackUrl(url);
        
        console.log('State updated - currentVideo:', video.title);
        console.log('State updated - playbackUrl:', url);

        // Scroll to the player
        window.scrollTo({top: 0, behavior: 'smooth'});
        
    } catch (err: any) {
        console.error('Failed to get playback URL:', err);
        alert('Failed to load video for playback');
    }
};


  return (
        <div style= {{ padding: '20px' }}>
          <h1>Connor's Video Streaming Service</h1>
           
            {/* Video Player - shows when a video is selected */}
            {currentVideo && playbackUrl && (
                <div style={{
                    marginBottom: '30px',
                    padding: '20px',
                    backgroundColor: '#f5f5f5',
                    borderRadius: '8px'
                }}>
                  <VideoPlayer playbackUrl={playbackUrl}title={currentVideo.title} />
                  <button
                    onClick={() => { setCurrentVideo(null); setPlaybackUrl(''); }}
                    style={{marginTop: '10px',
                            padding: '8px 16px',
                            backgroundColor: '#dc3545',
                            color: 'white',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: 'pointer'
                        }}
                        >Close Player 
                        </button>
                </div>
            )}
          {/* Upload Form */}
            <VideoUploadForm />
            
            {/* Refresh button to reload videos after upload */}
            <button
                onClick={fetchVideos}
                style={{
                    padding: '10px 20px',
                    margin: '20px 0',
                    fontSize: '16px',
                    backgroundColor: '#28a745',
                    color: 'white',
                    border: 'none',
                    borderRadius: '4px',
                    cursor: 'pointer'
                }}
            >
                Refresh Videos
            </button>
          <h2>My Videos</h2>
            {/* Render a loading message if the page is loading */}
            {loading && <p>Loading videos...</p>}
            {/* Render the setError message in red if no videos can be loaded */}
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {/* If loading is done and there are no videos, show a message prompting the user to upload some videos from the backend */}
            {!loading && videos.length === 0 && (<p>
              No videos yet. Upload one from your backend!</p>)}
              {/* Map each video in videos to a VideoCard component and display them in a flexbox grid */}
              <div style={{display: 'flex', flexWrap: 'wrap'}}>
                {videos.map(video => 
                  (<Videocard key={video.id} video={video}  onPlayClick={handlePlayClick}/>))}
              </div>
        </div>
    );
}

export default App
