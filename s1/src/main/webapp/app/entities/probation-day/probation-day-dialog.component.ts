import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ProbationDay } from './probation-day.model';
import { ProbationDayPopupService } from './probation-day-popup.service';
import { ProbationDayService } from './probation-day.service';

@Component({
    selector: 'jhi-probation-day-dialog',
    templateUrl: './probation-day-dialog.component.html'
})
export class ProbationDayDialogComponent implements OnInit {

    probationDay: ProbationDay;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private probationDayService: ProbationDayService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.probationDay.id !== undefined) {
            this.subscribeToSaveResponse(
                this.probationDayService.update(this.probationDay));
        } else {
            this.subscribeToSaveResponse(
                this.probationDayService.create(this.probationDay));
        }
    }

    private subscribeToSaveResponse(result: Observable<ProbationDay>) {
        result.subscribe((res: ProbationDay) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ProbationDay) {
        this.eventManager.broadcast({ name: 'probationDayListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-probation-day-popup',
    template: ''
})
export class ProbationDayPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private probationDayPopupService: ProbationDayPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.probationDayPopupService
                    .open(ProbationDayDialogComponent as Component, params['id']);
            } else {
                this.probationDayPopupService
                    .open(ProbationDayDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
