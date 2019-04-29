/**
 * Created by GFOLGOAS on 29/04/2019.
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ContextElement, TabContext, TabTarget } from '../../shared/util/insight-util';

@Injectable({ providedIn: 'root' })
export class UserContextService {
    userContext: TabContext[] = [];

    constructor(private http: HttpClient) {
        this.userContext.push(
            new TabContext('MAP', new Map()),
            new TabContext('NETWORK', new Map()),
            new TabContext('DASHBOARD', new Map()),
            new TabContext('CARD', new Map()),
            new TabContext('SOURCES', new Map())
        );
    }

    updateContext(tabTarget: TabTarget, ...items: ContextElement<any>[]) {
        const tabCtx: TabContext = this.userContext.find(tab => tab.tabTarget === tabTarget);
        items.forEach(element => {
            tabCtx.context.set(element.key, element);
        });
    }

    getTabContext(tabTarget: TabTarget): TabContext {
        return this.userContext.find(tab => tab.tabTarget === tabTarget);
    }
}
