import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Prefix } from './prefix.model';
import { PrefixPopupService } from './prefix-popup.service';
import { PrefixService } from './prefix.service';

@Component({
    selector: 'jhi-prefix-delete-dialog',
    templateUrl: './prefix-delete-dialog.component.html'
})
export class PrefixDeleteDialogComponent {

    prefix: Prefix;

    constructor(
        private prefixService: PrefixService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.prefixService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'prefixListModification',
                content: 'Deleted an prefix'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-prefix-delete-popup',
    template: ''
})
export class PrefixDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private prefixPopupService: PrefixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.prefixPopupService
                .open(PrefixDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
