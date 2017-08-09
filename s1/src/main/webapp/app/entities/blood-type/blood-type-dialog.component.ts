import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { BloodType } from './blood-type.model';
import { BloodTypePopupService } from './blood-type-popup.service';
import { BloodTypeService } from './blood-type.service';

@Component({
    selector: 'jhi-blood-type-dialog',
    templateUrl: './blood-type-dialog.component.html'
})
export class BloodTypeDialogComponent implements OnInit {

    bloodType: BloodType;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private bloodTypeService: BloodTypeService,
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
        if (this.bloodType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.bloodTypeService.update(this.bloodType));
        } else {
            this.subscribeToSaveResponse(
                this.bloodTypeService.create(this.bloodType));
        }
    }

    private subscribeToSaveResponse(result: Observable<BloodType>) {
        result.subscribe((res: BloodType) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: BloodType) {
        this.eventManager.broadcast({ name: 'bloodTypeListModification', content: 'OK'});
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
    selector: 'jhi-blood-type-popup',
    template: ''
})
export class BloodTypePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bloodTypePopupService: BloodTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.bloodTypePopupService
                    .open(BloodTypeDialogComponent as Component, params['id']);
            } else {
                this.bloodTypePopupService
                    .open(BloodTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
