/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { S1TestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PrefixDetailComponent } from '../../../../../../main/webapp/app/entities/prefix/prefix-detail.component';
import { PrefixService } from '../../../../../../main/webapp/app/entities/prefix/prefix.service';
import { Prefix } from '../../../../../../main/webapp/app/entities/prefix/prefix.model';

describe('Component Tests', () => {

    describe('Prefix Management Detail Component', () => {
        let comp: PrefixDetailComponent;
        let fixture: ComponentFixture<PrefixDetailComponent>;
        let service: PrefixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [S1TestModule],
                declarations: [PrefixDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PrefixService,
                    JhiEventManager
                ]
            }).overrideTemplate(PrefixDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PrefixDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PrefixService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Prefix(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.prefix).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
