export interface Video {
    id: number;
    title: string;
    description: string;
    status: 'UPLOADED' | 'PROCESSING' | 'READY' | 'FAILED';
    lengthSeconds: number;
    userId: number;
    uploadedAt: string;
    processedAt: string | null;
    originalFilename: string;
    storageKey: string | null;
    width: number | null;
    height: number | null;
    codec: string | null;
    fileSizeBytes: number | null;
}