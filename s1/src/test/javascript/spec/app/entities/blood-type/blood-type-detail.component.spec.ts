/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { S1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BloodTypeDetailComponent } from '../../../../../../main/webapp/app/entities/blood-type/blood-type-detail.component';
import { BloodTypeService } from '../../../../../../main/webapp/app/entities/blood-type/blood-type.service';
import { BloodType } from '../../../../../../main/webapp/app/entities/blood-type/blood-type.model';

describe('Component Tests', () => {

    describe('BloodType Management Detail Component', () => {
        let comp: BloodTypeDetailComponent;
        let fixture: ComponentFixture<BloodTypeDetailComponent>;
        let service: BloodTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [S1TestModule],
                declarations: [BloodTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BloodTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(BloodTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BloodTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BloodTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new BloodType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.bloodType).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
