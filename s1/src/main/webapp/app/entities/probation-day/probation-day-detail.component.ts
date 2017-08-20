import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { ProbationDay } from './probation-day.model';
import { ProbationDayService } from './probation-day.service';

@Component({
    selector: 'jhi-probation-day-detail',
    templateUrl: './probation-day-detail.component.html'
})
export class ProbationDayDetailComponent implements OnInit, OnDestroy {

    probationDay: ProbationDay;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private probationDayService: ProbationDayService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProbationDays();
    }

    load(id) {
        this.probationDayService.find(id).subscribe((probationDay) => {
            this.probationDay = probationDay;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProbationDays() {
        this.eventSubscriber = this.eventManager.subscribe(
            'probationDayListModification',
            (response) => this.load(this.probationDay.id)
        );
    }
}
