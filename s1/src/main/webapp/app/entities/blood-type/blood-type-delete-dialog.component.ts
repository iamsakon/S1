import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { BloodType } from './blood-type.model';
import { BloodTypePopupService } from './blood-type-popup.service';
import { BloodTypeService } from './blood-type.service';

@Component({
    selector: 'jhi-blood-type-delete-dialog',
    templateUrl: './blood-type-delete-dialog.component.html'
})
export class BloodTypeDeleteDialogComponent {

    bloodType: BloodType;

    constructor(
        private bloodTypeService: BloodTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.bloodTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'bloodTypeListModification',
                content: 'Deleted an bloodType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-blood-type-delete-popup',
    template: ''
})
export class BloodTypeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bloodTypePopupService: BloodTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.bloodTypePopupService
                .open(BloodTypeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
