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
    mongoBioId?: string;
    name?: string;
    surname?: string;
    processStatus?: IProcessStatus;
}

export interface IProcessStatus extends IDifferentialProcessStatus {
    urlHitCount?: number;
    imageHitCount?: number;
}

export interface IDifferentialProcessStatus {
    urlHitCountDiff?: DiffStatus;
    imageHitCountDiff?: DiffStatus;
}

export type DiffStatus = 'changed' | 'pause' | 'stable';
