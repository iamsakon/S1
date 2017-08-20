import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { EmployeeContact } from './employee-contact.model';
import { EmployeeContactPopupService } from './employee-contact-popup.service';
import { EmployeeContactService } from './employee-contact.service';
import { Employee, EmployeeService } from '../employee';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-employee-contact-dialog',
    templateUrl: './employee-contact-dialog.component.html'
})
export class EmployeeContactDialogComponent implements OnInit {

    employeeContact: EmployeeContact;
    isSaving: boolean;

    employees: Employee[];
    activeDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private employeeContactService: EmployeeContactService,
        private employeeService: EmployeeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.employeeService.query()
            .subscribe((res: ResponseWrapper) => { this.employees = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.employeeContact.id !== undefined) {
            this.subscribeToSaveResponse(
                this.employeeContactService.update(this.employeeContact));
        } else {
            this.subscribeToSaveResponse(
                this.employeeContactService.create(this.employeeContact));
        }
    }

    private subscribeToSaveResponse(result: Observable<EmployeeContact>) {
        result.subscribe((res: EmployeeContact) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: EmployeeContact) {
        this.eventManager.broadcast({ name: 'employeeContactListModification', content: 'OK'});
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

    trackEmployeeById(index: number, item: Employee) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-employee-contact-popup',
    template: ''
})
export class EmployeeContactPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private employeeContactPopupService: EmployeeContactPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.employeeContactPopupService
                    .open(EmployeeContactDialogComponent as Component, params['id']);
            } else {
                this.employeeContactPopupService
                    .open(EmployeeContactDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
