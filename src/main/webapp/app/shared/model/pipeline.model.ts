export interface ILoadedFormFile {
    id: string;
    file: File;
    isSended: boolean;
    isRead: boolean;
    fileName?: string;
    fileContent?: string;
}

export interface IProcessedFormFile {
    externalBioId: string;
    name?: string;
    surname?: string;
    processStatus?: any;
}
