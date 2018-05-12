/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DemocaipTestModule } from '../../../test.module';
import { StoryComponent } from '../../../../../../main/webapp/app/entities/story/story.component';
import { StoryService } from '../../../../../../main/webapp/app/entities/story/story.service';
import { Story } from '../../../../../../main/webapp/app/entities/story/story.model';

describe('Component Tests', () => {

    describe('Story Management Component', () => {
        let comp: StoryComponent;
        let fixture: ComponentFixture<StoryComponent>;
        let service: StoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DemocaipTestModule],
                declarations: [StoryComponent],
                providers: [
                    StoryService
                ]
            })
            .overrideTemplate(StoryComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Story(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.stories[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
