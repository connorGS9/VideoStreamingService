import type { Video } from '../types/Video';

interface VideocardProps {
    video : Video;
    onPlayClick?: (videoId: number) => void;
}

function Videocard({ video, onPlayClick} : VideocardProps) {
    const formatDuration = (seconds: number | null): string => {
        if (!seconds) return '0:00';
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60; 
        return `${mins}:${secs.toString().padStart(2, '0')}`;
    }

    const getStatusColor = (status: string): string => {
        switch (status) {
            case 'READY': return 'green';
            case 'PROCESSING': return 'orange';
            case 'UPLOADED': return 'blue';
            case 'FAILED': return 'red';
            default: return 'gray';
        }
    };

    return (
        <div style={{
            border:'1px solid #ddd',
            borderRadius:'8px',
            padding:'16px',
            margin:'16px',
            maxWidth:'300px',
        }}>
            <h3 style={{ cursor: 'pointer', color: 'blue' }}>
                {video.title}
            </h3>
            <p>{video.description}</p>
            <div style={{ marginTop: '8px', fontSize: '14px', color: '#666' }}>
                <p>Duration: {formatDuration(video.lengthSeconds)}</p>
                <p>Uploaded: {new Date(video.uploadedAt).toLocaleDateString()}</p>
                <p style={{ color: getStatusColor(video.status) }}>
                    Status: {video.status}
                </p>
                <p>Size: {(video.fileSizeBytes || 0) / 1000000} MB</p>
            </div>

            {/* Show Play button only if video is READY */}
            {video.status === 'READY' && onPlayClick &&  (
                <button
                onClick={() => onPlayClick(video.id)}
                style={{ marginTop: '10px',
                        padding: '8px 16px',
                        backgroundColor: '#007bff',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer',
                        fontSize: '14px'
                    }}
                    >
                        ▶ Play
                    </button>
            )}
        </div>
    );
}

export default Videocard;
