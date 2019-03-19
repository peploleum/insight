import { Component, OnInit } from '@angular/core';
import { SideMediatorService } from './side-mediator.service';
import { Subscription } from 'rxjs/index';
import { IRawData } from '../shared/model/raw-data.model';
import { RawDataService } from '../entities/raw-data/raw-data.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import { QuickViewService } from './quick-view.service';
import { ENTITY_TYPE_LIST } from '../shared/util/insight-util';

@Component({
    selector: 'ins-quick-annotation',
    templateUrl: './quick-annotation.component.html',
    styles: []
})
export class QuickAnnotationComponent implements OnInit {
    entity: IRawData;
    selected_data_ids: string[] = [];
    selectedDataSubs: Subscription;
    entityForm: FormGroup;
    entityTypeList = ENTITY_TYPE_LIST;

    constructor(
        private _sms: SideMediatorService,
        private _rds: RawDataService,
        private _fb: FormBuilder,
        private _qvs: QuickViewService
    ) {}

    ngOnInit() {
        this.entityForm = this._fb.group({
            entityType: ['', Validators.required],
            entityName: ['', Validators.required],
            entityPosition: [0, Validators.required]
        });
        this.selectedDataSubs = this._sms._selectedData.subscribe((ids: string[]) => {
            this.selected_data_ids = ids;
            if (this.selected_data_ids && this.selected_data_ids.length) {
                this.getEntity(this.selected_data_ids[0]);
            } else {
                this.entity = null;
            }
        });
    }

    getEntity(id: string) {
        this._rds.find(id).subscribe(
            (res: HttpResponse<IRawData>) => {
                this.entity = res.body;
            },
            error => {
                console.log('The entity with id: ' + id + ' is not a rawData or does not exist.');
            }
        );
    }

    onTextSelectionChange(selection: { textSelected: string; position: number }) {
        this.entityForm.controls['entityName'].setValue(selection.textSelected);
        this.entityForm.controls['entityPosition'].setValue(selection.position);
    }

    saveEntity() {
        if (this.entityForm.valid) {
            const entity = { entityType: '', entityName: '', entityPosition: 0, idRawData: '' };
            Object.keys(this.entityForm.value).forEach(key => (entity[key] = this.entityForm.value[key]));
            entity.idRawData = this.entity.id;
            this._qvs.saveAnnotatedEntity(entity, this.entity).subscribe(
                (res: HttpResponse<IRawData>) => {
                    this.entity = res.body;
                    this.entityForm.reset();
                },
                error => console.log('Failed to save the new RawData annotations.')
            );
        }
    }
}
