import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S1SharedModule } from '../../shared';
import {
    CandidateService,
    CandidatePopupService,
    CandidateComponent,
    CandidateDetailComponent,
    CandidateDialogComponent,
    CandidatePopupComponent,
    CandidateDeletePopupComponent,
    CandidateDeleteDialogComponent,
    candidateRoute,
    candidatePopupRoute,
    CandidateResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...candidateRoute,
    ...candidatePopupRoute,
];

@NgModule({
    imports: [
        S1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CandidateComponent,
        CandidateDetailComponent,
        CandidateDialogComponent,
        CandidateDeleteDialogComponent,
        CandidatePopupComponent,
        CandidateDeletePopupComponent,
    ],
    entryComponents: [
        CandidateComponent,
        CandidateDialogComponent,
        CandidatePopupComponent,
        CandidateDeleteDialogComponent,
        CandidateDeletePopupComponent,
    ],
    providers: [
        CandidateService,
        CandidatePopupService,
        CandidateResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1CandidateModule {}
