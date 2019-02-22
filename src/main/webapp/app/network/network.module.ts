import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NetworkRoutingModule } from './network-routing.module';
import { NetworkComponent } from './network.component';
import { NetworkMenuComponent } from './network-menu.component';
import { InsightSharedModule } from '../shared/shared.module';
import { NetworkSideMenuComponent } from './network-side-menu.component';
import { NetworkSideMenuDirective } from './network-side-menu.directive';
import { NetworkSearchComponent } from './side/network-search.component';
import { NetworkContentComponent } from './side/network-content.component';

@NgModule({
    imports: [CommonModule, NetworkRoutingModule, InsightSharedModule],
    declarations: [
        NetworkComponent,
        NetworkMenuComponent,
        NetworkSideMenuComponent,
        NetworkSideMenuDirective,
        NetworkSearchComponent,
        NetworkContentComponent
    ],
    entryComponents: [NetworkMenuComponent, NetworkSideMenuComponent],
    exports: [NetworkSearchComponent, NetworkContentComponent]
})
export class NetworkModule {}
