import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S1SharedModule } from '../../shared';
import {
    ProbationPeriodService,
    ProbationPeriodPopupService,
    ProbationPeriodComponent,
    ProbationPeriodDetailComponent,
    ProbationPeriodDialogComponent,
    ProbationPeriodPopupComponent,
    ProbationPeriodDeletePopupComponent,
    ProbationPeriodDeleteDialogComponent,
    probationPeriodRoute,
    probationPeriodPopupRoute,
    ProbationPeriodResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...probationPeriodRoute,
    ...probationPeriodPopupRoute,
];

@NgModule({
    imports: [
        S1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProbationPeriodComponent,
        ProbationPeriodDetailComponent,
        ProbationPeriodDialogComponent,
        ProbationPeriodDeleteDialogComponent,
        ProbationPeriodPopupComponent,
        ProbationPeriodDeletePopupComponent,
    ],
    entryComponents: [
        ProbationPeriodComponent,
        ProbationPeriodDialogComponent,
        ProbationPeriodPopupComponent,
        ProbationPeriodDeleteDialogComponent,
        ProbationPeriodDeletePopupComponent,
    ],
    providers: [
        ProbationPeriodService,
        ProbationPeriodPopupService,
        ProbationPeriodResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1ProbationPeriodModule {}
