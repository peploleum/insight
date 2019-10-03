export interface ILoadedFormFile {
    file: File;
    fileName?: string;
}

export interface IProcessedFormFile {
    externalBioId: string;
    name?: string;
    surname?: string;
    processStatus: any;
}
