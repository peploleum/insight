import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEquipment } from 'app/shared/model/equipment.model';

@Component({
    selector: 'jhi-equipment-detail',
    templateUrl: './equipment-detail.component.html'
})
export class EquipmentDetailComponent implements OnInit {
    equipment: IEquipment;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ equipment }) => {
            this.equipment = equipment;
        });
    }

    previousState() {
        window.history.back();
    }
}
