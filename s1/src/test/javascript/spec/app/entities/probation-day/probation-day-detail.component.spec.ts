/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { S1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProbationDayDetailComponent } from '../../../../../../main/webapp/app/entities/probation-day/probation-day-detail.component';
import { ProbationDayService } from '../../../../../../main/webapp/app/entities/probation-day/probation-day.service';
import { ProbationDay } from '../../../../../../main/webapp/app/entities/probation-day/probation-day.model';

describe('Component Tests', () => {

    describe('ProbationDay Management Detail Component', () => {
        let comp: ProbationDayDetailComponent;
        let fixture: ComponentFixture<ProbationDayDetailComponent>;
        let service: ProbationDayService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [S1TestModule],
                declarations: [ProbationDayDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProbationDayService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProbationDayDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProbationDayDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProbationDayService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProbationDay(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.probationDay).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
