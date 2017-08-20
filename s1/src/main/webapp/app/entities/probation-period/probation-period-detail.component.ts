import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { ProbationPeriod } from './probation-period.model';
import { ProbationPeriodService } from './probation-period.service';

@Component({
    selector: 'jhi-probation-period-detail',
    templateUrl: './probation-period-detail.component.html'
})
export class ProbationPeriodDetailComponent implements OnInit, OnDestroy {

    probationPeriod: ProbationPeriod;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private probationPeriodService: ProbationPeriodService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProbationPeriods();
    }

    load(id) {
        this.probationPeriodService.find(id).subscribe((probationPeriod) => {
            this.probationPeriod = probationPeriod;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProbationPeriods() {
        this.eventSubscriber = this.eventManager.subscribe(
            'probationPeriodListModification',
            (response) => this.load(this.probationPeriod.id)
        );
    }
}
