import { Component, OnDestroy, OnInit } from '@angular/core';
import { PipelineService } from 'app/pipeline/pipeline.service';
import { interval, Observable, of, Subscription, zip } from 'rxjs';
import { ILoadedFormFile, IProcessedFormFile } from 'app/shared/model/pipeline.model';
import { readFile } from 'app/shared/util/insight-util';
import { JhiAlertService } from 'ng-jhipster';
import { startWith, switchMap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { DictionaryService } from 'app/dictionary/dictionary.service';
import { IDictionary } from 'app/shared/model/analytics.model';

@Component({
    selector: 'ins-pipeline',
    templateUrl: './pipeline.component.html',
    styleUrls: ['pipeline.component.scss']
})
export class PipelineComponent implements OnInit, OnDestroy {
    loadedFilesSubs: Subscription;
    processStatusSubs: Subscription;

    dictionaries: IDictionary[] = [];
    selectedDictionaryId: string;

    constructor(private _ps: PipelineService, private _ds: DictionaryService, private _jas: JhiAlertService) {}

    ngOnInit() {
        this._ds.getAll().subscribe(res => {
            this.dictionaries = res.body;
            if (this.dictionaries && this.dictionaries.length > 0) {
                this.selectedDictionaryId = this.dictionaries[0].id;
            }
        });
        this.loadedFilesSubs = this._ps.loadedFiles.subscribe((files: ILoadedFormFile[]) => {
            files
                .filter(f => !f.fileContent && !f.isRead)
                .map(f => {
                    f.isRead = true;
                    return f;
                })
                .forEach(f => {
                    readFile<any>(f.file).then(
                        content => {
                            f.fileContent = content;
                        },
                        error => {
                            this.onError(`Failed to read file content: ${f.fileName}`);
                        }
                    );
                });
        });
        if (this.getProcessedFiles().length > 0) {
            this.startProcessStatusRefresher();
        }
    }

    ngOnDestroy(): void {
        this.loadedFilesSubs.unsubscribe();
        if (this.processStatusSubs) {
            this.processStatusSubs.unsubscribe();
        }
    }

    sendForms() {
        this._ps.loadedFiles
            .getValue()
            .filter(f => !f.isSended && f.fileContent)
            .forEach(f => {
                f.fileContent['idDictionary'] = this.selectedDictionaryId;
                this.sendForm(f);
            });
    }

    startProcessStatusRefresher() {
        if (this.processStatusSubs) {
            return;
        }
        this.processStatusSubs = interval(5000)
            .pipe(
                startWith(),
                switchMap(() => this.fakeRefresh())
            )
            .subscribe(
                (result: IProcessedFormFile[]) => {
                    result.forEach(file => {
                        const pff = this.getProcessedFiles().find(f => f.externalBioId === file.externalBioId);
                        if (pff && pff.processStatus) {
                            file.processStatus.urlHitCountDiff =
                                pff.processStatus.urlHitCount !== file.processStatus.urlHitCount
                                    ? 'changed'
                                    : pff.processStatus.urlHitCountDiff === 'changed'
                                    ? 'pause'
                                    : 'stable';
                            file.processStatus.imageHitCountDiff =
                                pff.processStatus.imageHitCount !== file.processStatus.imageHitCount
                                    ? 'changed'
                                    : pff.processStatus.imageHitCountDiff === 'changed'
                                    ? 'pause'
                                    : 'stable';
                        }
                    });
                    this._ps.processedFiles.next(result);
                },
                error => {
                    this.onError('Error while refreshing the pipeline status.');
                }
            );
    }

    refresh(): Observable<IProcessedFormFile[]> {
        return zip(...this.getProcessedFiles().map(val => this._ps.getProcessStatus(val)));
    }

    fakeRefresh(): Observable<IProcessedFormFile[]> {
        return zip(
            ...this.getProcessedFiles().map(val => {
                const random1 = Math.random() >= 0.5;
                const random2 = Math.random() >= 0.5;
                const urlHitCount = val.processStatus ? (random1 ? val.processStatus.urlHitCount + 1 : val.processStatus.urlHitCount) : 0;
                const imageHitCount = val.processStatus
                    ? random2
                        ? val.processStatus.imageHitCount + 1
                        : val.processStatus.imageHitCount
                    : 0;
                const copy: IProcessedFormFile = {
                    externalBioId: val.externalBioId,
                    mongoBioId: val.mongoBioId,
                    surname: 'bobby',
                    name: 'brian',
                    processStatus: {
                        urlHitCount,
                        imageHitCount,
                        urlHitCountDiff: val.processStatus ? val.processStatus.urlHitCountDiff || 'stable' : 'stable',
                        imageHitCountDiff: val.processStatus ? val.processStatus.imageHitCountDiff || 'stable' : 'stable'
                    }
                };
                return of(copy);
            })
        );
    }

    clearLoadedForms() {
        this._ps.loadedFiles.next([]);
    }

    sendForm(file: ILoadedFormFile) {
        file.isSended = true;
        this._ps.sendForm(file.fileContent).subscribe(
            (res: HttpResponse<string>) => {
                const externalBioId = res.body;
                this._ps.processedFiles.next(this.getProcessedFiles().concat([{ externalBioId }]));
                this._ps.loadedFiles.next(this.getLoadedFiles().filter(f => f.id === file.id));
                this.startProcessStatusRefresher();
            },
            error => {
                this.onError(`Failed to post file content: ${file.fileName}`);
            }
        );
    }

    getProcessedFiles(): IProcessedFormFile[] {
        return this._ps.processedFiles.getValue();
    }

    getLoadedFiles(): ILoadedFormFile[] {
        return this._ps.loadedFiles.getValue();
    }

    protected onError(errorMessage: string) {
        this._jas.error(errorMessage, null, null);
    }
}
