import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EmployeeContact } from './employee-contact.model';
import { EmployeeContactService } from './employee-contact.service';

@Injectable()
export class EmployeeContactPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private employeeContactService: EmployeeContactService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.employeeContactService.find(id).subscribe((employeeContact) => {
                    if (employeeContact.activeDate) {
                        employeeContact.activeDate = {
                            year: employeeContact.activeDate.getFullYear(),
                            month: employeeContact.activeDate.getMonth() + 1,
                            day: employeeContact.activeDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.employeeContactModalRef(component, employeeContact);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.employeeContactModalRef(component, new EmployeeContact());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    employeeContactModalRef(component: Component, employeeContact: EmployeeContact): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.employeeContact = employeeContact;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
