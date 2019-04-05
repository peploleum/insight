import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InsightSearchComponent } from './insight-search.component';

describe('InsightSearchComponent', () => {
    let component: InsightSearchComponent;
    let fixture: ComponentFixture<InsightSearchComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [InsightSearchComponent]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(InsightSearchComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
