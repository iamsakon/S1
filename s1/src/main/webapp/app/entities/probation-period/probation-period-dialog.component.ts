import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProbationPeriod } from './probation-period.model';
import { ProbationPeriodPopupService } from './probation-period-popup.service';
import { ProbationPeriodService } from './probation-period.service';
import { ProbationDay, ProbationDayService } from '../probation-day';
import { Employee, EmployeeService } from '../employee';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-probation-period-dialog',
    templateUrl: './probation-period-dialog.component.html'
})
export class ProbationPeriodDialogComponent implements OnInit {

    probationPeriod: ProbationPeriod;
    isSaving: boolean;

    probationdays: ProbationDay[];

    employees: Employee[];
    periodStartDp: any;
    periodEndDp: any;
    evaluatePeriodStartDp: any;
    evaluatePeriodEndDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private probationPeriodService: ProbationPeriodService,
        private probationDayService: ProbationDayService,
        private employeeService: EmployeeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.probationDayService.query()
            .subscribe((res: ResponseWrapper) => { this.probationdays = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.employeeService.query()
            .subscribe((res: ResponseWrapper) => { this.employees = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.probationPeriod.id !== undefined) {
            this.subscribeToSaveResponse(
                this.probationPeriodService.update(this.probationPeriod));
        } else {
            this.subscribeToSaveResponse(
                this.probationPeriodService.create(this.probationPeriod));
        }
    }

    private subscribeToSaveResponse(result: Observable<ProbationPeriod>) {
        result.subscribe((res: ProbationPeriod) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ProbationPeriod) {
        this.eventManager.broadcast({ name: 'probationPeriodListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackProbationDayById(index: number, item: ProbationDay) {
        return item.id;
    }

    trackEmployeeById(index: number, item: Employee) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-probation-period-popup',
    template: ''
})
export class ProbationPeriodPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private probationPeriodPopupService: ProbationPeriodPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.probationPeriodPopupService
                    .open(ProbationPeriodDialogComponent as Component, params['id']);
            } else {
                this.probationPeriodPopupService
                    .open(ProbationPeriodDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
