/**
 * Created by gFolgoas on 31/01/2019.
 */
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs/internal/operators';
import { SideInterface } from '../../shared/side/side.abstract';
import { MapService } from '../map.service';
import { DrawingLayer, FigureStyle } from '../../shared/util/map-utils';

@Component({
    selector: 'ins-dessin',
    templateUrl: './dessin.component.html'
})
export class DessinComponent extends SideInterface implements OnInit, OnDestroy {
    @Input()
    layerList: DrawingLayer[] = [new DrawingLayer('bidou', 'test', true)];

    currentStyle: FigureStyle;
    dessinForm: FormGroup;

    constructor(protected formBuilder: FormBuilder, protected ms: MapService) {
        super();
        this.currentStyle = this.ms.dessinStates.getValue();
        this.dessinForm = this.formBuilder.group({
            form: [this.currentStyle.form, Validators.required],
            size: [this.currentStyle.size, Validators.required],
            type: [this.currentStyle.type, Validators.required],
            strokeColor: [this.currentStyle.strokeColor, Validators.required],
            fillColor: [this.currentStyle.fillColor, Validators.required]
        });
        this.dessinForm.valueChanges
            .pipe(
                debounceTime(200),
                distinctUntilChanged()
            )
            .subscribe(value => {
                if (this.dessinForm.valid) {
                    this.currentStyle = new FigureStyle(
                        value['form'],
                        value['size'],
                        value['type'],
                        value['strokeColor'],
                        value['fillColor']
                    );
                    this.ms.dessinStates.next(this.currentStyle);
                }
            });
    }

    ngOnInit() {}

    ngOnDestroy() {}

    onFormSelected(form: string) {
        this.f.form.setValue(form);
    }

    get f() {
        return this.dessinForm.controls;
    }
}
