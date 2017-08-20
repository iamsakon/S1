/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { S1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProbationPeriodDetailComponent } from '../../../../../../main/webapp/app/entities/probation-period/probation-period-detail.component';
import { ProbationPeriodService } from '../../../../../../main/webapp/app/entities/probation-period/probation-period.service';
import { ProbationPeriod } from '../../../../../../main/webapp/app/entities/probation-period/probation-period.model';

describe('Component Tests', () => {

    describe('ProbationPeriod Management Detail Component', () => {
        let comp: ProbationPeriodDetailComponent;
        let fixture: ComponentFixture<ProbationPeriodDetailComponent>;
        let service: ProbationPeriodService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [S1TestModule],
                declarations: [ProbationPeriodDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProbationPeriodService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProbationPeriodDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProbationPeriodDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProbationPeriodService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProbationPeriod(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.probationPeriod).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
