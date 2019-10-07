import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ILoadedFormFile } from 'app/shared/model/pipeline.model';
import { PipelineService } from 'app/pipeline/pipeline.service';
import { FileSystemDirectoryEntry, FileSystemFileEntry, UploadEvent } from 'ngx-file-drop';
import { UUID } from 'app/shared/util/insight-util';

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

    dropped(event: UploadEvent) {
        this.displayDropZone = false;
        // const files: UploadFile[] = event.files;
        for (const droppedFile of event.files) {
            if (droppedFile.fileEntry.isFile) {
                const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
                fileEntry.file((file: File) => {
                    console.log(droppedFile.relativePath, file);
                    this.onFileLoaded(file);
                });
            } else {
                const fileEntry = droppedFile.fileEntry as FileSystemDirectoryEntry;
                console.log(droppedFile.relativePath, fileEntry);
                fileEntry.createReader().readEntries((result: FileSystemFileEntry[]) => {
                    result.forEach(value => {
                        value.file((file: File) => {
                            this.onFileLoaded(file);
                        });
                    });
                });
            }
        }
    }

    onFileLoaded(file: File) {
        const loadedFiles: ILoadedFormFile[] = this._ps.loadedFiles.getValue();
        this._ps.loadedFiles.next(
            loadedFiles.concat([
                {
                    id: UUID(),
                    file,
                    isSended: false,
                    isRead: false,
                    fileName: file.name
                }
            ])
        );
    }

    onFileOver(event) {
        this.displayDropZone = true;
    }

    onFileLeave(event) {
        this.displayDropZone = false;
    }
}
