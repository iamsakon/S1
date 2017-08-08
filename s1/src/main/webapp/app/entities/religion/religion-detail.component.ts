import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Religion } from './religion.model';
import { ReligionService } from './religion.service';

@Component({
    selector: 'jhi-religion-detail',
    templateUrl: './religion-detail.component.html'
})
export class ReligionDetailComponent implements OnInit, OnDestroy {

    religion: Religion;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private religionService: ReligionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReligions();
    }

    load(id) {
        this.religionService.find(id).subscribe((religion) => {
            this.religion = religion;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReligions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'religionListModification',
            (response) => this.load(this.religion.id)
        );
    }
}
