import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SchemaInfoComponent } from './schema-info.component';

describe('SchemaInfoComponent', () => {
    let component: SchemaInfoComponent;
    let fixture: ComponentFixture<SchemaInfoComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [SchemaInfoComponent]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(SchemaInfoComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
