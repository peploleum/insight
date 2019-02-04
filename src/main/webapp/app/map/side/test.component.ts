/**
 * Created by gFolgoas on 04/02/2019.
 */
/**
 * Created by gFolgoas on 31/01/2019.
 */
import { Component, OnDestroy, OnInit } from '@angular/core';
import { MapService } from '../map.service';
import { SideInterface } from '../../shared/side/side.abstract';
import { FormBuilder } from '@angular/forms';

@Component({
    selector: 'ins-test',
    templateUrl: './test.component.html'
})
export class TestComponent extends SideInterface implements OnInit, OnDestroy {
    constructor(protected formBuilder: FormBuilder, protected ms: MapService) {
        super();
    }

    ngOnInit() {}

    ngOnDestroy() {}
}
