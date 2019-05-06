import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InsightEntitySearchComponent } from './insight-entity-search.component';

describe('InsightEntitySearchComponent', () => {
    let component: InsightEntitySearchComponent;
    let fixture: ComponentFixture<InsightEntitySearchComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [InsightEntitySearchComponent]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(InsightEntitySearchComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
