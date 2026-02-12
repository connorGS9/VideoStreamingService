import type { Video } from '../types/Video';

interface VideocardProps {
    video : Video;
    onClick: () => void;
}

function Videocard({ video, onClick} : VideocardProps) {
    const formatDuration = (seconds: number | null): string => {
        if (!seconds) return '0:00';
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60; 
        return `${mins}:${secs.toString().padStart(2, '0')}`;
    }

    return (
        <div
            onClick={onClick}
            className="bg-dark rounded-lg overflow-hidden cursor-pointer hover:ring-2 hover:ring-primary transition group"
            >

            {/*Thumbnail placeholder */}
            <div className="bg-gray-800 aspect-video flex items-center justify-center relative" >

                 {/* Play Icon */}
                 <svg className="w-16 h-16 text-white opacity-70 group-hover:text-primary group-hover:opacity-100 transition" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M8 5v14l11-7z"/>
                </svg>

                 {/* Duration Badge */}
                 {video.lengthSeconds &&  (
                    <div className="absolute bottom-2 right-2 bg-black bg-opacity-80 px-2 py-1 rounded text-sm">
                        {formatDuration(video.lengthSeconds)}
                    </div>
                 )}
            </div>

            {/* Video info */}
            <div className="p-4">
                <h3 className="font-semibold text-white mb-1 line-clamp-2 group-hover:text-primary transition">
                    {video.title}
                </h3>
                <p className="text-sm text-gray-400 line-clamp-2 mb-2">
                    {video.description || 'No description'}
                </p>

                <div className="flex items-center gap-2 text-xs text-gray-500">
                    <span>{new Date(video.uploadedAt).toLocaleDateString()}</span>
                    {video.width && video.height && (
                        <>
                            <span>•</span>
                            <span>{video.width}x{video.height}</span>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Videocard;
