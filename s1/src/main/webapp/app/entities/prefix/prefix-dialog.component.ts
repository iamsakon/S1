import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Prefix } from './prefix.model';
import { PrefixPopupService } from './prefix-popup.service';
import { PrefixService } from './prefix.service';

@Component({
    selector: 'jhi-prefix-dialog',
    templateUrl: './prefix-dialog.component.html'
})
export class PrefixDialogComponent implements OnInit {

    prefix: Prefix;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private prefixService: PrefixService,
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
        if (this.prefix.id !== undefined) {
            this.subscribeToSaveResponse(
                this.prefixService.update(this.prefix));
        } else {
            this.subscribeToSaveResponse(
                this.prefixService.create(this.prefix));
        }
    }

    private subscribeToSaveResponse(result: Observable<Prefix>) {
        result.subscribe((res: Prefix) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Prefix) {
        this.eventManager.broadcast({ name: 'prefixListModification', content: 'OK'});
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
    selector: 'jhi-prefix-popup',
    template: ''
})
export class PrefixPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private prefixPopupService: PrefixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.prefixPopupService
                    .open(PrefixDialogComponent as Component, params['id']);
            } else {
                this.prefixPopupService
                    .open(PrefixDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
