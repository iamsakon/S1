import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Religion } from './religion.model';
import { ReligionPopupService } from './religion-popup.service';
import { ReligionService } from './religion.service';

@Component({
    selector: 'jhi-religion-delete-dialog',
    templateUrl: './religion-delete-dialog.component.html'
})
export class ReligionDeleteDialogComponent {

    religion: Religion;

    constructor(
        private religionService: ReligionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.religionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'religionListModification',
                content: 'Deleted an religion'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-religion-delete-popup',
    template: ''
})
export class ReligionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private religionPopupService: ReligionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.religionPopupService
                .open(ReligionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
