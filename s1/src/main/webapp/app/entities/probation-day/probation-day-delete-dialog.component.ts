import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProbationDay } from './probation-day.model';
import { ProbationDayPopupService } from './probation-day-popup.service';
import { ProbationDayService } from './probation-day.service';

@Component({
    selector: 'jhi-probation-day-delete-dialog',
    templateUrl: './probation-day-delete-dialog.component.html'
})
export class ProbationDayDeleteDialogComponent {

    probationDay: ProbationDay;

    constructor(
        private probationDayService: ProbationDayService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.probationDayService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'probationDayListModification',
                content: 'Deleted an probationDay'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-probation-day-delete-popup',
    template: ''
})
export class ProbationDayDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private probationDayPopupService: ProbationDayPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.probationDayPopupService
                .open(ProbationDayDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
