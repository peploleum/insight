import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IEquipment } from 'app/shared/model/equipment.model';

@Component({
    selector: 'ins-equipment-detail',
    templateUrl: './equipment-detail.component.html'
})
export class EquipmentDetailComponent implements OnInit {
    equipment: IEquipment;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ equipment }) => {
            this.equipment = equipment;
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
