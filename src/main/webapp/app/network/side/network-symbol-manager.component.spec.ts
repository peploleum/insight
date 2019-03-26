import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkSymbolManagerComponent } from './network-symbol-manager.component';

describe('NetworkSymbolManagerComponent', () => {
    let component: NetworkSymbolManagerComponent;
    let fixture: ComponentFixture<NetworkSymbolManagerComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [NetworkSymbolManagerComponent]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(NetworkSymbolManagerComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
