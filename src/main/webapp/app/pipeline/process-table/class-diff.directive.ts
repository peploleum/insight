import { Directive, HostBinding, Input } from '@angular/core';
import { DiffStatus } from 'app/shared/model/pipeline.model';

@Directive({
    selector: '[insClassDiff]'
})
export class ClassDiffDirective {
    @HostBinding('class.changed')
    changed: boolean;

    @HostBinding('class.pause')
    pause: boolean;

    @HostBinding('class.stable')
    stable: boolean;

    @Input()
    set differentialStatus(val: DiffStatus) {
        this.changed = val === 'changed';
        this.pause = val === 'pause';
        this.stable = val === 'stable';
    }

    // @HostBinding('class')
    // get differentialClasses(): string {
    //     return this.differentialStatus ? this.differentialStatus : '';
    // }

    constructor() {}
}
