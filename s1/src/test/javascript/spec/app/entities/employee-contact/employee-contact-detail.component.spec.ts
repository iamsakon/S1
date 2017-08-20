/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { S1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EmployeeContactDetailComponent } from '../../../../../../main/webapp/app/entities/employee-contact/employee-contact-detail.component';
import { EmployeeContactService } from '../../../../../../main/webapp/app/entities/employee-contact/employee-contact.service';
import { EmployeeContact } from '../../../../../../main/webapp/app/entities/employee-contact/employee-contact.model';

describe('Component Tests', () => {

    describe('EmployeeContact Management Detail Component', () => {
        let comp: EmployeeContactDetailComponent;
        let fixture: ComponentFixture<EmployeeContactDetailComponent>;
        let service: EmployeeContactService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [S1TestModule],
                declarations: [EmployeeContactDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EmployeeContactService,
                    JhiEventManager
                ]
            }).overrideTemplate(EmployeeContactDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EmployeeContactDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmployeeContactService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new EmployeeContact(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.employeeContact).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
