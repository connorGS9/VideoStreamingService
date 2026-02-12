import { useNavigate } from 'react-router-dom';
import VideoUploadForm from '../components/VideoUploadForm';

function UploadPage() {
    const navigate = useNavigate();

    return (
        <div className="min-h-screen bg-darker">
            {/* Header */}
            <div className="bg-dark border-b border-gray-800">
                <div className="max-w-7xl mx-auto px-8 py-6 flex items-center gap-4">
                    <button
                        onClick={() => navigate('/')}
                        className="text-gray-400 hover:text-white transition flex items-center gap-2"
                    >
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
                        </svg>
                        Back to Home
                    </button>
                    <h1 className="text-2xl font-bold">Upload Video</h1>
                </div>
            </div>

            {/* Upload Form */}
            <div className="max-w-3xl mx-auto px-8 py-12">
                <VideoUploadForm />
            </div>
        </div>
    );
}

export default UploadPage;