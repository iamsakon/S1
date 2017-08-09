import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { BloodType } from './blood-type.model';
import { BloodTypeService } from './blood-type.service';

@Component({
    selector: 'jhi-blood-type-detail',
    templateUrl: './blood-type-detail.component.html'
})
export class BloodTypeDetailComponent implements OnInit, OnDestroy {

    bloodType: BloodType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private bloodTypeService: BloodTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBloodTypes();
    }

    load(id) {
        this.bloodTypeService.find(id).subscribe((bloodType) => {
            this.bloodType = bloodType;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBloodTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'bloodTypeListModification',
            (response) => this.load(this.bloodType.id)
        );
    }
}
