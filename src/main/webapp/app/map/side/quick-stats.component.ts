/**
 * Created by gFolgoas on 31/01/2019.
 */
import { Component, OnDestroy, OnInit } from '@angular/core';
import { SideInterface } from '../../shared/side/side.abstract';
import { MapService } from '../map.service';

@Component({
    selector: 'ins-quick-stats',
    templateUrl: './quick-stats.component.html'
})
export class QuickStatsComponent extends SideInterface implements OnInit, OnDestroy {
    constructor(protected ms: MapService) {
        super();
    }

    ngOnInit() {}

    ngOnDestroy() {}
}
