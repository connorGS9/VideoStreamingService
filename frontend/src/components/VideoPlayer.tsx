import { use, useEffect, useRef } from 'react';
import { preload } from 'react-dom';
import videojs from 'video.js';
import 'video.js/dist/video-js.css';

interface VideoPlayerProps {
    playbackUrl: string;
    title: string;
}

function VideoPlayer({ playbackUrl, title }: VideoPlayerProps) {
    const videoRef = useRef<HTMLDivElement>(null);
    const playerRef = useRef<any>(null);

    useEffect(() => {
        console.log('=== VideoPlayer useEffect triggered ===');
        console.log('playbackUrl prop:', playbackUrl);
        console.log('playbackUrl type:', typeof playbackUrl);
        console.log('playbackUrl length:', playbackUrl?.length);
    
        // Make sure container exists
        if (!videoRef.current) {
            console.log('❌ Video container not ready yet');
            return;
        }
        
        if (!playbackUrl) {
            console.log('❌ No playbackUrl provided yet');
            return;
        }

        console.log('✅ Initializing player with URL:', playbackUrl);
            // Make sure container exists
            if (!videoRef.current) {
                console.log('Video container not ready yet');
                return;
            }

        console.log('Initializing player with URL:', playbackUrl);

        // Create video element
        const videoElement = document.createElement('video-js');
        videoElement.classList.add('video-js', 'vjs-big-play-centered');
        videoElement.setAttribute('controls', '');
        videoElement.setAttribute('preload', 'auto');
        
        // Clear container and add video element
        videoRef.current.innerHTML = '';
        videoRef.current.appendChild(videoElement);

        // Initialize Video.js
        const player = videojs(videoElement, {
            controls: true,
            autoplay: false,
            preload: 'auto',
            fluid: true,
            responsive: true,
            sources: [{
                src: playbackUrl,
                type: 'application/x-mpegURL'
            }]
        });

        playerRef.current = player;

        // Event listeners
        player.ready(() => {
            console.log('✅ Player is ready!');
        });

        player.on('loadstart', () => {
            console.log('Loading video...');
        });

        player.on('error', () => {
            const error = player.error();
            console.error('❌ Player error:', error);
        });

        player.on('loadedmetadata', () => {
            console.log('✅ Video metadata loaded');
        });

        // Cleanup
        return () => {
            console.log('Disposing player');
            if (playerRef.current) {
                playerRef.current.dispose();
                playerRef.current = null;
            }
        };
    }, [playbackUrl]);

    return (
        <div style={{ maxWidth: '800px', margin: '20px auto' }}>
            <h3 style={{ marginBottom: '10px' }}>{title}</h3>
            <div 
                ref={videoRef} 
                style={{
                    width: '100%',
                    backgroundColor: '#000'
                }}
            />
        </div>
    );
}
export default VideoPlayer;