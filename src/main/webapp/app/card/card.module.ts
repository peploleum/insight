import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardComponent } from './card.component';
import { InsightSharedModule } from '../shared/shared.module';
import { CardRoutingModule } from './card-routing.module';

@NgModule({
    imports: [CommonModule, InsightSharedModule, CardRoutingModule],
    declarations: [CardComponent]
})
export class CardModule {}
