/**
 * Created by gFolgoas on 31/01/2019.
 */
import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { SideInterface } from '../../shared/side/side.abstract';
import { MapService } from '../map.service';
import * as Chart from 'chart.js';

@Component({
    selector: 'ins-quick-stats',
    templateUrl: './quick-stats.component.html'
})
export class QuickStatsComponent extends SideInterface implements OnInit, AfterViewInit, OnDestroy {
    chart: Chart;
    @ViewChild('canvas') canvas: ElementRef;

    constructor(protected ms: MapService) {
        super();
    }

    ngOnInit() {}

    ngAfterViewInit() {
        this.chart = new Chart((<HTMLCanvasElement>this.canvas.nativeElement).getContext('2d'), {});
    }

    ngOnDestroy() {}
}
