import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { EmployeeContact } from './employee-contact.model';
import { EmployeeContactPopupService } from './employee-contact-popup.service';
import { EmployeeContactService } from './employee-contact.service';

@Component({
    selector: 'jhi-employee-contact-delete-dialog',
    templateUrl: './employee-contact-delete-dialog.component.html'
})
export class EmployeeContactDeleteDialogComponent {

    employeeContact: EmployeeContact;

    constructor(
        private employeeContactService: EmployeeContactService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.employeeContactService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'employeeContactListModification',
                content: 'Deleted an employeeContact'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-employee-contact-delete-popup',
    template: ''
})
export class EmployeeContactDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private employeeContactPopupService: EmployeeContactPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.employeeContactPopupService
                .open(EmployeeContactDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
