import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { EmployeeContact } from './employee-contact.model';
import { EmployeeContactService } from './employee-contact.service';

@Component({
    selector: 'jhi-employee-contact-detail',
    templateUrl: './employee-contact-detail.component.html'
})
export class EmployeeContactDetailComponent implements OnInit, OnDestroy {

    employeeContact: EmployeeContact;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private employeeContactService: EmployeeContactService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEmployeeContacts();
    }

    load(id) {
        this.employeeContactService.find(id).subscribe((employeeContact) => {
            this.employeeContact = employeeContact;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEmployeeContacts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'employeeContactListModification',
            (response) => this.load(this.employeeContact.id)
        );
    }
}
