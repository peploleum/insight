import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NetworkRoutingModule } from './network-routing.module';
import { NetworkComponent } from './network.component';
import { NetworkMenuComponent } from './network-menu.component';
import { InsightSharedModule } from '../shared/shared.module';
import { NetworkSideMenuComponent } from './network-side-menu.component';
import { NetworkSideMenuDirective } from './network-side-menu.directive';

@NgModule({
    declarations: [NetworkComponent, NetworkMenuComponent, NetworkSideMenuComponent, NetworkSideMenuDirective],
    imports: [CommonModule, NetworkRoutingModule, InsightSharedModule],
    entryComponents: [NetworkMenuComponent, NetworkSideMenuComponent]
})
export class NetworkModule {}
