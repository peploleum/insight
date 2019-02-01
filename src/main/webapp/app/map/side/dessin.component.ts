/**
 * Created by gFolgoas on 31/01/2019.
 */
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { debounceTime } from 'rxjs/internal/operators';
import { SideInterface } from '../../shared/side/side.abstract';

@Component({
    selector: 'ins-dessin',
    templateUrl: './dessin.component.html'
})
export class DessinComponent extends SideInterface implements OnInit, OnDestroy {
    @Input()
    layerList: DrawingLayer[] = [new DrawingLayer('bidou', 'test', true)];
    @Input()
    initialStyle: FigureStyle = new FigureStyle('Circle', 2, 10, 'red', 'blue');

    @Output()
    actionEmitter: EventEmitter<string> = new EventEmitter();
    @Output()
    styleEmitter: EventEmitter<FigureStyle> = new EventEmitter();

    dessinForm: FormGroup;

    strokeColor = 'red';
    fillColor = 'blue';

    constructor(private formBuilder: FormBuilder) {
        super();
        this.dessinForm = this.formBuilder.group({
            form: [this.initialStyle.form, Validators.required],
            size: [this.initialStyle.size, Validators.required],
            type: [this.initialStyle.type, Validators.required],
            strokeColor: [this.initialStyle.strokeColor, Validators.required],
            fillColor: [this.initialStyle.fillColor, Validators.required]
        });
        this.dessinForm.valueChanges.pipe(debounceTime(200)).subscribe(value => {
            if (this.dessinForm.valid) {
                this.styleEmitter.emit(
                    new FigureStyle(value['form'], value['size'], value['type'], value['strokeColor'], value['fillColor'])
                );
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

    sendAction(action: string) {
        this.actionEmitter.emit(action);
    }

    colorChanged($event, type: string) {
        if (type === 'StrokeColor') {
            this.f.strokeColor.setValue(this.strokeColor);
        } else {
            this.f.fillColor.setValue(this.fillColor);
        }
    }
}

export class DrawingLayer {
    layerId: string;
    layerName: string;
    selected: boolean;

    constructor(layerId: string, layerName: string, selected: boolean) {
        this.layerId = layerId;
        this.layerName = layerName;
        this.selected = selected;
    }
}

export class FigureStyle {
    form: string;
    size: number;
    type: number;
    strokeColor: string;
    fillColor?: string;

    constructor(form: string, size: number, type: number, strokeColor: string, fillColor?: string) {
        this.form = form;
        this.size = size;
        this.type = type;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
    }
}
