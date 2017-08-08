/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { S1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MilitaryStatusDetailComponent } from '../../../../../../main/webapp/app/entities/military-status/military-status-detail.component';
import { MilitaryStatusService } from '../../../../../../main/webapp/app/entities/military-status/military-status.service';
import { MilitaryStatus } from '../../../../../../main/webapp/app/entities/military-status/military-status.model';

describe('Component Tests', () => {

    describe('MilitaryStatus Management Detail Component', () => {
        let comp: MilitaryStatusDetailComponent;
        let fixture: ComponentFixture<MilitaryStatusDetailComponent>;
        let service: MilitaryStatusService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [S1TestModule],
                declarations: [MilitaryStatusDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MilitaryStatusService,
                    JhiEventManager
                ]
            }).overrideTemplate(MilitaryStatusDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MilitaryStatusDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MilitaryStatusService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new MilitaryStatus(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.militaryStatus).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
