import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ILoadedFormFile } from 'app/shared/model/pipeline.model';
import { PipelineService } from 'app/pipeline/pipeline.service';
import { FileSystemDirectoryEntry, FileSystemFileEntry, UploadEvent, UploadFile } from 'ngx-file-drop';

@Component({
    selector: 'ins-load-table',
    templateUrl: './load-table.component.html',
    styleUrls: ['load-table.component.scss']
})
export class LoadTableComponent implements OnInit {
    displayDropZone = false;

    constructor(private _ps: PipelineService) {}

    ngOnInit() {}

    getLoadedFormFiles(): Observable<ILoadedFormFile[]> {
        return this._ps.loadedFiles;
    }

    clearList() {
        this._ps.loadedFiles.next([]);
    }

    dropped(event: UploadEvent) {
        const files: UploadFile[] = event.files;
        for (const droppedFile of event.files) {
            // Is it a file?
            if (droppedFile.fileEntry.isFile) {
                const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
                fileEntry.file((file: File) => {
                    // Here you can access the real file
                    console.log(droppedFile.relativePath, file);
                });
            } else {
                // It was a directory (empty directories are added, otherwise only files)
                const fileEntry = droppedFile.fileEntry as FileSystemDirectoryEntry;
                console.log(droppedFile.relativePath, fileEntry);
            }
        }
    }

    onFileOver(event) {
        this.displayDropZone = true;
    }

    onFileLeave(event) {
        this.displayDropZone = false;
    }
}
