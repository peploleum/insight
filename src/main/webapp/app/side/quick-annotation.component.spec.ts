import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QuickAnnotationComponent } from './quick-annotation.component';

describe('QuickAnnotationComponent', () => {
    let component: QuickAnnotationComponent;
    let fixture: ComponentFixture<QuickAnnotationComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [QuickAnnotationComponent]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(QuickAnnotationComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
