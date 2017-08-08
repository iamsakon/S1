import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Prefix } from './prefix.model';
import { PrefixService } from './prefix.service';

@Component({
    selector: 'jhi-prefix-detail',
    templateUrl: './prefix-detail.component.html'
})
export class PrefixDetailComponent implements OnInit, OnDestroy {

    prefix: Prefix;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private prefixService: PrefixService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPrefixes();
    }

    load(id) {
        this.prefixService.find(id).subscribe((prefix) => {
            this.prefix = prefix;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPrefixes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'prefixListModification',
            (response) => this.load(this.prefix.id)
        );
    }
}
