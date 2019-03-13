/**
 * Created by gFolgoas on 17/01/2019.
 */
import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
import { NetworkService } from './network.service';

@Component({
    selector: 'ins-network-side-menu',
    templateUrl: './network-side-menu.component.html'
})
export class NetworkSideMenuComponent implements OnInit {
    networkStates;
    elementName: string;
    dimension: { top: number; right: number; height: number } = { top: 0, right: 0, height: 0 };
    selectedJSONFile: File;

    constructor(@Inject('directiveActionEmitter') private actionEmitter: EventEmitter<string>, private _ns: NetworkService) {}

    ngOnInit() {}

    sendAction(action: string) {
        this.actionEmitter.emit(action);
    }

    onFileInputChange(event: any) {
        if (event.target.files && event.target.files.length) {
            const [file] = event.target.files;
            this.selectedJSONFile = file;
        } else {
            this.selectedJSONFile = null;
        }
    }

    importJsonFile() {
        if (this.selectedJSONFile) {
            this._ns.JSONFileSelected.next(this.selectedJSONFile);
        }
    }
}
