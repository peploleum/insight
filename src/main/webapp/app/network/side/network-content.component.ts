/**
 * Created by gFolgoas on 20/02/2019.
 */
import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { SideInterface } from '../../side/side.abstract';
import { Subscription } from 'rxjs/index';
import { NetworkService } from '../network.service';
import { DataContentInfo, NetworkState } from '../../shared/util/network.util';
import { toKebabCase } from '../../shared/util/insight-util';
import { FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs/internal/operators';
import { SideMediatorService } from '../../side/side-mediator.service';

@Component({
    selector: 'ins-network-content',
    templateUrl: './network-content.component.html'
})
export class NetworkContentComponent extends SideInterface implements OnInit, AfterViewInit, OnDestroy {
    networkContent: DataContentInfo[];
    searchForm: FormControl = new FormControl('');
    currentSearch = '';
    searchProperties: string[] = ['label'];
    networkStates: NetworkState;
    selectedJSONFile: File;
    selectedIds: string[] = [];

    private _networkContentSubs: Subscription;
    private _networkStateSubs: Subscription;
    private _selectedDataSubs: Subscription;

    constructor(private _ns: NetworkService, private _sms: SideMediatorService) {
        super();
    }

    ngOnInit() {
        setTimeout(() => {
            this._networkContentSubs = this._ns.networkDataContent.subscribe((newContent: DataContentInfo[]) => {
                this.networkContent = newContent;
            });
        });
        this._networkStateSubs = this._ns.networkState.subscribe(state => (this.networkStates = state));
        this.searchForm.valueChanges
            .pipe(
                debounceTime(500),
                distinctUntilChanged()
            )
            .subscribe((value: any) => {
                this.currentSearch = value;
                this.filterList();
            });
        this._selectedDataSubs = this._sms._selectedData.subscribe((ids: string[]) => {
            this.selectedIds = ids;
        });
    }

    ngAfterViewInit() {}

    ngOnDestroy() {
        if (this._networkContentSubs) {
            this._networkContentSubs.unsubscribe();
        }
        if (this._networkStateSubs) {
            this._networkStateSubs.unsubscribe();
        }
        if (this._selectedDataSubs) {
            this._selectedDataSubs.unsubscribe();
        }
    }

    filterList() {}

    getLink(str: string): string {
        return toKebabCase(str);
    }

    trackByFn(index, data: DataContentInfo): string {
        return data.idMongo;
    }

    sendAction(action: string) {
        this._sms._onNewActionClicked.next(action);
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

    onDataClicked(id: string) {
        this._sms._onNewDataSelected.next([id]);
    }
}
