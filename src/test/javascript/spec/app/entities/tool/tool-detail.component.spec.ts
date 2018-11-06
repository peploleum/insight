/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { ToolDetailComponent } from 'app/entities/tool/tool-detail.component';
import { Tool } from 'app/shared/model/tool.model';

describe('Component Tests', () => {
    describe('Tool Management Detail Component', () => {
        let comp: ToolDetailComponent;
        let fixture: ComponentFixture<ToolDetailComponent>;
        const route = ({ data: of({ tool: new Tool(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [ToolDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ToolDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ToolDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.tool).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
