import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ProbationPeriod } from './probation-period.model';
import { ProbationPeriodService } from './probation-period.service';

@Injectable()
export class ProbationPeriodPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private probationPeriodService: ProbationPeriodService

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
                this.probationPeriodService.find(id).subscribe((probationPeriod) => {
                    if (probationPeriod.periodStart) {
                        probationPeriod.periodStart = {
                            year: probationPeriod.periodStart.getFullYear(),
                            month: probationPeriod.periodStart.getMonth() + 1,
                            day: probationPeriod.periodStart.getDate()
                        };
                    }
                    if (probationPeriod.periodEnd) {
                        probationPeriod.periodEnd = {
                            year: probationPeriod.periodEnd.getFullYear(),
                            month: probationPeriod.periodEnd.getMonth() + 1,
                            day: probationPeriod.periodEnd.getDate()
                        };
                    }
                    if (probationPeriod.evaluatePeriodStart) {
                        probationPeriod.evaluatePeriodStart = {
                            year: probationPeriod.evaluatePeriodStart.getFullYear(),
                            month: probationPeriod.evaluatePeriodStart.getMonth() + 1,
                            day: probationPeriod.evaluatePeriodStart.getDate()
                        };
                    }
                    if (probationPeriod.evaluatePeriodEnd) {
                        probationPeriod.evaluatePeriodEnd = {
                            year: probationPeriod.evaluatePeriodEnd.getFullYear(),
                            month: probationPeriod.evaluatePeriodEnd.getMonth() + 1,
                            day: probationPeriod.evaluatePeriodEnd.getDate()
                        };
                    }
                    this.ngbModalRef = this.probationPeriodModalRef(component, probationPeriod);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.probationPeriodModalRef(component, new ProbationPeriod());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    probationPeriodModalRef(component: Component, probationPeriod: ProbationPeriod): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.probationPeriod = probationPeriod;
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
