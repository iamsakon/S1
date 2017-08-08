/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { S1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ReligionDetailComponent } from '../../../../../../main/webapp/app/entities/religion/religion-detail.component';
import { ReligionService } from '../../../../../../main/webapp/app/entities/religion/religion.service';
import { Religion } from '../../../../../../main/webapp/app/entities/religion/religion.model';

describe('Component Tests', () => {

    describe('Religion Management Detail Component', () => {
        let comp: ReligionDetailComponent;
        let fixture: ComponentFixture<ReligionDetailComponent>;
        let service: ReligionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [S1TestModule],
                declarations: [ReligionDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ReligionService,
                    JhiEventManager
                ]
            }).overrideTemplate(ReligionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ReligionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReligionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Religion(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.religion).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
