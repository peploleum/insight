import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SourcesManagerComponent } from './sources-manager.component';

describe('SourcesManagerComponent', () => {
    let component: SourcesManagerComponent;
    let fixture: ComponentFixture<SourcesManagerComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [SourcesManagerComponent]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(SourcesManagerComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
