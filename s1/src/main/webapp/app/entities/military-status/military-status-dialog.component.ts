import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MilitaryStatus } from './military-status.model';
import { MilitaryStatusPopupService } from './military-status-popup.service';
import { MilitaryStatusService } from './military-status.service';

@Component({
    selector: 'jhi-military-status-dialog',
    templateUrl: './military-status-dialog.component.html'
})
export class MilitaryStatusDialogComponent implements OnInit {

    militaryStatus: MilitaryStatus;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private militaryStatusService: MilitaryStatusService,
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
        if (this.militaryStatus.id !== undefined) {
            this.subscribeToSaveResponse(
                this.militaryStatusService.update(this.militaryStatus));
        } else {
            this.subscribeToSaveResponse(
                this.militaryStatusService.create(this.militaryStatus));
        }
    }

    private subscribeToSaveResponse(result: Observable<MilitaryStatus>) {
        result.subscribe((res: MilitaryStatus) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: MilitaryStatus) {
        this.eventManager.broadcast({ name: 'militaryStatusListModification', content: 'OK'});
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
    selector: 'jhi-military-status-popup',
    template: ''
})
export class MilitaryStatusPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private militaryStatusPopupService: MilitaryStatusPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.militaryStatusPopupService
                    .open(MilitaryStatusDialogComponent as Component, params['id']);
            } else {
                this.militaryStatusPopupService
                    .open(MilitaryStatusDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
