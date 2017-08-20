import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProbationPeriod } from './probation-period.model';
import { ProbationPeriodPopupService } from './probation-period-popup.service';
import { ProbationPeriodService } from './probation-period.service';

@Component({
    selector: 'jhi-probation-period-delete-dialog',
    templateUrl: './probation-period-delete-dialog.component.html'
})
export class ProbationPeriodDeleteDialogComponent {

    probationPeriod: ProbationPeriod;

    constructor(
        private probationPeriodService: ProbationPeriodService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.probationPeriodService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'probationPeriodListModification',
                content: 'Deleted an probationPeriod'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-probation-period-delete-popup',
    template: ''
})
export class ProbationPeriodDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private probationPeriodPopupService: ProbationPeriodPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.probationPeriodPopupService
                .open(ProbationPeriodDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
