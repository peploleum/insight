import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IRawData } from 'app/shared/model/raw-data.model';

@Component({
    selector: 'ins-analytics-detail',
    templateUrl: './analytics-detail.component.html'
})
export class AnalyticsDetailComponent implements OnInit {
    rawData: IRawData;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rawData }) => {
            this.rawData = rawData;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    previousState() {
        window.history.back();
    }
}
