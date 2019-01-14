import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NetworkRoutingModule } from './network-routing.module';
import { NetworkComponent } from './network.component';
import { NetworkMenuComponent } from './network-menu.component';

@NgModule({
    declarations: [NetworkComponent, NetworkMenuComponent],
    imports: [CommonModule, NetworkRoutingModule],
    entryComponents: [NetworkMenuComponent]
})
export class NetworkModule {}
