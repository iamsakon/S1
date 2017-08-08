import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MilitaryStatus } from './military-status.model';
import { MilitaryStatusPopupService } from './military-status-popup.service';
import { MilitaryStatusService } from './military-status.service';

@Component({
    selector: 'jhi-military-status-delete-dialog',
    templateUrl: './military-status-delete-dialog.component.html'
})
export class MilitaryStatusDeleteDialogComponent {

    militaryStatus: MilitaryStatus;

    constructor(
        private militaryStatusService: MilitaryStatusService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.militaryStatusService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'militaryStatusListModification',
                content: 'Deleted an militaryStatus'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-military-status-delete-popup',
    template: ''
})
export class MilitaryStatusDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private militaryStatusPopupService: MilitaryStatusPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.militaryStatusPopupService
                .open(MilitaryStatusDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
