import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NetworkRoutingModule } from './network-routing.module';
import { NetworkComponent } from './network.component';

@NgModule({
  imports: [
    CommonModule,
    NetworkRoutingModule
  ],
  declarations: [NetworkComponent]
})
export class NetworkModule { }
