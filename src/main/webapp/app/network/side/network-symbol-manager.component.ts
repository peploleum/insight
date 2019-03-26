import { Component, OnInit } from '@angular/core';
import { NetworkSymbol } from '../../shared/util/network.util';
import { JhiDataUtils } from 'ng-jhipster';
import { UUID } from '../../shared/util/insight-util';
import symbols = Mocha.reporters.Base.symbols;

@Component({
    selector: 'ins-network-symbol-manager',
    templateUrl: './network-symbol-manager.component.html',
    styles: []
})
export class NetworkSymbolManagerComponent implements OnInit {
    symbols: NetworkSymbol[] = [];
    validImageTypes = ['image/jpeg', 'image/png'];

    constructor(private dataUtils: JhiDataUtils) {}

    ngOnInit() {}

    onFileInputChange(event: any) {
        if (event.target.files && event.target.files.length) {
            const newSymbol: NetworkSymbol = new NetworkSymbol(UUID());
            const file = event.target.files[0];
            if (this.validImageTypes.indexOf(file.type) === -1) {
                return;
            }
            this.dataUtils.toBase64(file, base64Data => {
                const type = file.type;
                newSymbol.base64 = `data:${type};base64,` + base64Data;
                this.symbols = this.symbols.concat([newSymbol]);
            });
        }
    }
}
