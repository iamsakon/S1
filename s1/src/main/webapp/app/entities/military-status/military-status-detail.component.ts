import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { MilitaryStatus } from './military-status.model';
import { MilitaryStatusService } from './military-status.service';

@Component({
    selector: 'jhi-military-status-detail',
    templateUrl: './military-status-detail.component.html'
})
export class MilitaryStatusDetailComponent implements OnInit, OnDestroy {

    militaryStatus: MilitaryStatus;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private militaryStatusService: MilitaryStatusService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMilitaryStatuses();
    }

    load(id) {
        this.militaryStatusService.find(id).subscribe((militaryStatus) => {
            this.militaryStatus = militaryStatus;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMilitaryStatuses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'militaryStatusListModification',
            (response) => this.load(this.militaryStatus.id)
        );
    }
}
