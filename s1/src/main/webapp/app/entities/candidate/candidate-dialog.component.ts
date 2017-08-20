import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Candidate } from './candidate.model';
import { CandidatePopupService } from './candidate-popup.service';
import { CandidateService } from './candidate.service';
import { Prefix, PrefixService } from '../prefix';
import { Gender, GenderService } from '../gender';
import { BloodType, BloodTypeService } from '../blood-type';
import { Nationality, NationalityService } from '../nationality';
import { Race, RaceService } from '../race';
import { Religion, ReligionService } from '../religion';
import { MilitaryStatus, MilitaryStatusService } from '../military-status';
import { MaritalStatus, MaritalStatusService } from '../marital-status';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-candidate-dialog',
    templateUrl: './candidate-dialog.component.html'
})
export class CandidateDialogComponent implements OnInit {

    candidate: Candidate;
    isSaving: boolean;

    prefixes: Prefix[];

    genders: Gender[];

    bloodtypes: BloodType[];

    nationalities: Nationality[];

    races: Race[];

    religions: Religion[];

    militarystatuses: MilitaryStatus[];

    maritalstatuses: MaritalStatus[];
    birthDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private candidateService: CandidateService,
        private prefixService: PrefixService,
        private genderService: GenderService,
        private bloodTypeService: BloodTypeService,
        private nationalityService: NationalityService,
        private raceService: RaceService,
        private religionService: ReligionService,
        private militaryStatusService: MilitaryStatusService,
        private maritalStatusService: MaritalStatusService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.prefixService.query()
            .subscribe((res: ResponseWrapper) => { this.prefixes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.genderService.query()
            .subscribe((res: ResponseWrapper) => { this.genders = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.bloodTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.bloodtypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.nationalityService.query()
            .subscribe((res: ResponseWrapper) => { this.nationalities = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.raceService.query()
            .subscribe((res: ResponseWrapper) => { this.races = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.religionService.query()
            .subscribe((res: ResponseWrapper) => { this.religions = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.militaryStatusService.query()
            .subscribe((res: ResponseWrapper) => { this.militarystatuses = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.maritalStatusService.query()
            .subscribe((res: ResponseWrapper) => { this.maritalstatuses = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.candidate.id !== undefined) {
            this.subscribeToSaveResponse(
                this.candidateService.update(this.candidate));
        } else {
            this.subscribeToSaveResponse(
                this.candidateService.create(this.candidate));
        }
    }

    private subscribeToSaveResponse(result: Observable<Candidate>) {
        result.subscribe((res: Candidate) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Candidate) {
        this.eventManager.broadcast({ name: 'candidateListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackPrefixById(index: number, item: Prefix) {
        return item.id;
    }

    trackGenderById(index: number, item: Gender) {
        return item.id;
    }

    trackBloodTypeById(index: number, item: BloodType) {
        return item.id;
    }

    trackNationalityById(index: number, item: Nationality) {
        return item.id;
    }

    trackRaceById(index: number, item: Race) {
        return item.id;
    }

    trackReligionById(index: number, item: Religion) {
        return item.id;
    }

    trackMilitaryStatusById(index: number, item: MilitaryStatus) {
        return item.id;
    }

    trackMaritalStatusById(index: number, item: MaritalStatus) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-candidate-popup',
    template: ''
})
export class CandidatePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private candidatePopupService: CandidatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.candidatePopupService
                    .open(CandidateDialogComponent as Component, params['id']);
            } else {
                this.candidatePopupService
                    .open(CandidateDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
