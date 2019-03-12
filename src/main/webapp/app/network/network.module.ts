import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NetworkRoutingModule } from './network-routing.module';
import { NetworkComponent } from './network.component';
import { NetworkMenuComponent } from './network-menu.component';
import { InsightSharedModule } from '../shared/shared.module';
import { NetworkSideMenuComponent } from './network-side-menu.component';
import { NetworkSideMenuDirective } from './network-side-menu.directive';
import { NetworkContentComponent } from './side/network-content.component';
import { ReactiveFormsModule } from '@angular/forms';
import { NeighborViewComponent } from './inner-window/neighbor-view.component';

@NgModule({
    imports: [CommonModule, NetworkRoutingModule, InsightSharedModule, ReactiveFormsModule],
    declarations: [
        NetworkComponent,
        NetworkMenuComponent,
        NetworkSideMenuComponent,
        NetworkSideMenuDirective,
        NetworkContentComponent,
        NeighborViewComponent
    ],
    entryComponents: [NetworkMenuComponent, NetworkSideMenuComponent],
    exports: [NetworkContentComponent]
})
export class NetworkModule {}
