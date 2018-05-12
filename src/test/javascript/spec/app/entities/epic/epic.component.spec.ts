/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DemocaipTestModule } from '../../../test.module';
import { EpicComponent } from '../../../../../../main/webapp/app/entities/epic/epic.component';
import { EpicService } from '../../../../../../main/webapp/app/entities/epic/epic.service';
import { Epic } from '../../../../../../main/webapp/app/entities/epic/epic.model';

describe('Component Tests', () => {

    describe('Epic Management Component', () => {
        let comp: EpicComponent;
        let fixture: ComponentFixture<EpicComponent>;
        let service: EpicService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DemocaipTestModule],
                declarations: [EpicComponent],
                providers: [
                    EpicService
                ]
            })
            .overrideTemplate(EpicComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EpicComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EpicService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Epic(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.epics[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
