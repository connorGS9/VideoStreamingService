import { useState } from 'react';
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api'

function VideoUploadForm() {
    const [file, setFile] = useState<File | null>(null);
    const [title, setTitle] = useState<string>('');
    const [description, setDescription] = useState<string>('');
    const [userId, setUserId] = useState<number>(1); // Hardcoded for now
    const [uploading, setUploading] = useState<boolean>(false);
    const [message, setMessage] = useState<string>('');

     const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            setFile(e.target.files[0]);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault(); // Prevent page reload

        if (!file || !title) {
            setMessage('Please select a file and enter a title');
            return;
        }

         try {
            setUploading(true);
            setMessage('');

            // Create FormData (like multipart/form-data in Postman)
            const formData = new FormData();
            formData.append('file', file);
            formData.append('title', title);
            formData.append('description', description);
            formData.append('userId', userId.toString());

            // Upload to backend
            const response = await axios.post(
                `${API_BASE_URL}/videos/upload`,
                formData,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                }
            );

            setMessage(`Video uploaded successfully! ID: ${response.data.id}`);
            
            // Clear form
            setFile(null);
            setTitle('');
            setDescription('');
            
            // Reset file input
            const fileInput = document.getElementById('file-input') as HTMLInputElement;
            if (fileInput) fileInput.value = '';

        } catch (error: any) {
            console.error('Upload failed:', error);
            setMessage(`Upload failed: ${error.message}`);
        } finally {
            setUploading(false);
        }
    };

    return (
        <div style={{
            border: '2px solid #ddd',
            borderRadius: '8px',
            padding: '20px',
            margin: '20px 0',
            maxWidth: '500px'
        }}>
            <h2>Upload Video</h2>
            
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '15px' }}>
                    <label style={{ display: 'block', marginBottom: '5px' }}>
                        Video File:
                    </label>
                    <input
                        id="file-input"
                        type="file"
                        accept="video/*"
                        onChange={handleFileChange}
                        disabled={uploading}
                    />
                    {file && <p style={{ fontSize: '14px', color: '#666' }}>
                        Selected: {file.name} ({(file.size / 1000000).toFixed(2)} MB)
                    </p>}
                </div>

                <div style={{ marginBottom: '15px' }}>
                    <label style={{ display: 'block', marginBottom: '5px' }}>
                        Title:
                    </label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        placeholder="Enter video title"
                        disabled={uploading}
                        style={{
                            width: '100%',
                            padding: '8px',
                            fontSize: '14px'
                        }}
                    />
                </div>

                <div style={{ marginBottom: '15px' }}>
                    <label style={{ display: 'block', marginBottom: '5px' }}>
                        Description:
                    </label>
                    <textarea
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder="Enter video description (optional)"
                        disabled={uploading}
                        rows={3}
                        style={{
                            width: '100%',
                            padding: '8px',
                            fontSize: '14px'
                        }}
                    />
                </div>

                <button
                    type="submit"
                    disabled={uploading || !file || !title}
                    style={{
                        padding: '10px 20px',
                        fontSize: '16px',
                        backgroundColor: uploading ? '#ccc' : '#007bff',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: uploading ? 'not-allowed' : 'pointer'
                    }}
                >
                    {uploading ? 'Uploading...' : 'Upload Video'}
                </button>
            </form>

            {message && (
                <p style={{
                    marginTop: '15px',
                    padding: '10px',
                    backgroundColor: message.includes('failed') ? '#ffebee' : '#e8f5e9',
                    color: message.includes('failed') ? '#c62828' : '#2e7d32',
                    borderRadius: '4px'
                }}>
                    {message}
                </p>
            )}
        </div>
    );
}

export default VideoUploadForm;
