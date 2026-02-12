# Video Streaming Service
A full-stack video streaming platform with automatic HLS transcoding, built with Spring Boot and React.


## Features

- **Video Upload** - Multipart file upload with progress tracking
- **Automatic Processing** - Background worker transcodes videos to HLS format using FFmpeg
- **Adaptive Streaming** - HLS (HTTP Live Streaming) for smooth playback across devices
- **Cloud Storage** - AWS S3 integration for scalable video storage
- **Modern UI** - Responsive React interface with Tailwind CSS
- **Real-time Updates** - Videos automatically transition from UPLOADED → PROCESSING → READY

## Tech Stack

### Backend
- **Java 17** - Core language
- **Spring Boot 3.x** - REST API framework
- **PostgreSQL** - Relational database
- **AWS S3** - Cloud object storage
- **FFmpeg** - Video transcoding and processing
- **Hibernate/JPA** - ORM for database operations

### Frontend
- **React 18** - UI framework
- **TypeScript** - Type-safe JavaScript
- **React Router** - Client-side routing
- **Axios** - HTTP client
- **Tailwind CSS** - Utility-first styling
- **Video.js** - HLS video player

### DevOps & Tools
- **Maven** - Build automation
- **Vite** - Frontend build tool
- **Git** - Version control

## Architecture
```
User uploads video
    ↓
Spring Boot API saves to S3 (raw/)
    ↓
Database record created (status: UPLOADED)
    ↓
Background worker detects new upload
    ↓
FFmpeg transcodes to HLS format
    ↓
Processed files saved to S3 (processed/)
    ↓
Status updated to READY
    ↓
Frontend streams via Video.js player
```

## API Endpoints
```
GET    /api/videos           - List all videos
GET    /api/videos/{id}      - Get video details
POST   /api/videos/upload    - Upload new video
GET    /api/videos/{id}/play - Get playback URL
```

## Video Processing Pipeline

1. **Upload**: Original video stored in S3 at `raw/{videoId}/`
2. **Metadata Extraction**: FFprobe extracts duration, resolution, codec
3. **Transcoding**: FFmpeg converts to HLS (720p, 4-second segments)
4. **Storage**: Playlist + segments saved to `processed/{videoId}/`
5. **Playback**: Public S3 URLs enable streaming

## Database Schema
```sql
videos
- id (BIGINT, Primary Key)
- title (VARCHAR)
- description (TEXT)
- status (ENUM: UPLOADED, PROCESSING, READY, FAILED)
- user_id (BIGINT)
- storage_key (VARCHAR)
- length_seconds (INTEGER)
- width (INTEGER)
- height (INTEGER)
- codec (VARCHAR)
- file_size_bytes (BIGINT)
- uploaded_at (TIMESTAMP)
- processed_at (TIMESTAMP)
```

## Running Locally

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL
- FFmpeg installed and in PATH
- AWS account with S3 bucket

### Backend Setup

1. Clone the repository
```bash
git clone https://github.com/yourusername/VideoStreamingService.git
cd VideoStreamingService/Backend
```

2. Configure `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password

aws.s3.bucketName=your-bucket-name
aws.accessKeyId=your-access-key
aws.secretAccessKey=your-secret-key
aws.region=us-east-1
```

3. Run the application
```bash
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`

### Frontend Setup

1. Navigate to frontend
```bash
cd ../frontend
```

2. Install dependencies
```bash
npm install
```

3. Run development server
```bash
npm run dev
```

Frontend runs on `http://localhost:5173`

## Project Structure
```
VideoStreamingService/
├── Backend/
│   ├── src/main/java/com/connorGS9/videoStreamingService/
│   │   ├── controller/     # REST API endpoints
│   │   ├── service/        # Business logic
│   │   ├── repository/     # Database access
│   │   ├── model/          # JPA entities
│   │   ├── dto/            # Data transfer objects
│   │   ├── config/         # Configuration classes
│   │   └── worker/         # Background processing
│   └── pom.xml
└── frontend/
    ├── src/
    │   ├── components/     # Reusable UI components
    │   ├── pages/          # Route pages
    │   ├── services/       # API integration
    │   └── types/          # TypeScript interfaces
    └── package.json
```

## Key Features Implemented

### Backend
- ✅ RESTful API design
- ✅ Multipart file upload handling
- ✅ Scheduled background processing
- ✅ FFmpeg integration for video transcoding
- ✅ AWS S3 SDK integration
- ✅ CORS configuration
- ✅ Exception handling
- ✅ Database relationships and queries

### Frontend
- ✅ React Router for multi-page navigation
- ✅ TypeScript for type safety
- ✅ Custom React hooks (useState, useEffect, useRef)
- ✅ Axios for HTTP requests
- ✅ Video.js HLS player integration
- ✅ Responsive grid layouts
- ✅ Form handling with file upload
- ✅ Progress tracking during upload

## Future Enhancements

- [ ] User authentication (JWT)
- [ ] Multiple quality levels (1080p, 480p, 360p)
- [ ] Video thumbnails generation
- [ ] Search and filtering
- [ ] Playlist functionality
- [ ] Comments and ratings
- [ ] CloudFront CDN integration
- [ ] Video analytics dashboard
- [ ] Drag-and-drop upload
- [ ] Dark/light mode toggle

## Challenges Overcome

- **S3 Path Management**: Resolved leading slash issues in object keys
- **CORS Configuration**: Enabled cross-origin requests between frontend and backend
- **FFmpeg Process Blocking**: Implemented stream readers to prevent hanging
- **Video.js Integration**: Configured HLS playback with proper MIME types
- **TypeScript Type Safety**: Created interfaces matching backend DTOs

## Learning Outcomes

- Full-stack development with modern frameworks
- Cloud storage integration (AWS S3)
- Video processing and streaming protocols
- Background job processing
- RESTful API design
- React component architecture
- TypeScript for frontend development

## License

MIT

## Author

Connor - [GitHub](https://github.com/connorGS9)
