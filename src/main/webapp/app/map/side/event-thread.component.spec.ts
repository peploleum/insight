import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EventThreadComponent } from './event-thread.component';

describe('EventThreadComponent', () => {
    let component: EventThreadComponent;
    let fixture: ComponentFixture<EventThreadComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [EventThreadComponent]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(EventThreadComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
