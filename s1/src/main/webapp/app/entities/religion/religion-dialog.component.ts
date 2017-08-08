import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Religion } from './religion.model';
import { ReligionPopupService } from './religion-popup.service';
import { ReligionService } from './religion.service';

@Component({
    selector: 'jhi-religion-dialog',
    templateUrl: './religion-dialog.component.html'
})
export class ReligionDialogComponent implements OnInit {

    religion: Religion;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private religionService: ReligionService,
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
        if (this.religion.id !== undefined) {
            this.subscribeToSaveResponse(
                this.religionService.update(this.religion));
        } else {
            this.subscribeToSaveResponse(
                this.religionService.create(this.religion));
        }
    }

    private subscribeToSaveResponse(result: Observable<Religion>) {
        result.subscribe((res: Religion) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Religion) {
        this.eventManager.broadcast({ name: 'religionListModification', content: 'OK'});
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
    selector: 'jhi-religion-popup',
    template: ''
})
export class ReligionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private religionPopupService: ReligionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.religionPopupService
                    .open(ReligionDialogComponent as Component, params['id']);
            } else {
                this.religionPopupService
                    .open(ReligionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
