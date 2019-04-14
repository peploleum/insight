import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OverlayToolbarComponent } from './overlay-toolbar.component';

describe('OverlayToolbarComponent', () => {
    let component: OverlayToolbarComponent;
    let fixture: ComponentFixture<OverlayToolbarComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [OverlayToolbarComponent]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(OverlayToolbarComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
