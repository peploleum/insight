import { Component, OnInit } from '@angular/core';
import { PipelineService } from 'app/pipeline/pipeline.service';
import { Observable } from 'rxjs';
import { IProcessedFormFile } from 'app/shared/model/pipeline.model';
import { animate, state, style, transition, trigger } from '@angular/animations';

@Component({
    selector: 'ins-process-table',
    templateUrl: './process-table.component.html',
    styleUrls: ['process-table.component.scss'],
    animations: [
        trigger('diffColor', [
            state(
                'changed',
                style({
                    backgroundColor: 'rgba(255, 51, 51, 0.1)'
                })
            ),
            state(
                'pause',
                style({
                    backgroundColor: 'rgba(51, 255, 51, 0.1)'
                })
            ),
            state('stable', style({})),
            transition('void => changed, void => stable, void => pause', [animate('2000ms ease-out')])
        ])
    ]
})
export class ProcessTableComponent implements OnInit {
    constructor(private _ps: PipelineService) {}

    ngOnInit() {}

    getProcessedFiles(): Observable<IProcessedFormFile[]> {
        return this._ps.processedFiles;
    }
}
