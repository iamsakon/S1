import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S1SharedModule } from '../../shared';
import {
    ProbationDayService,
    ProbationDayPopupService,
    ProbationDayComponent,
    ProbationDayDetailComponent,
    ProbationDayDialogComponent,
    ProbationDayPopupComponent,
    ProbationDayDeletePopupComponent,
    ProbationDayDeleteDialogComponent,
    probationDayRoute,
    probationDayPopupRoute,
    ProbationDayResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...probationDayRoute,
    ...probationDayPopupRoute,
];

@NgModule({
    imports: [
        S1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProbationDayComponent,
        ProbationDayDetailComponent,
        ProbationDayDialogComponent,
        ProbationDayDeleteDialogComponent,
        ProbationDayPopupComponent,
        ProbationDayDeletePopupComponent,
    ],
    entryComponents: [
        ProbationDayComponent,
        ProbationDayDialogComponent,
        ProbationDayPopupComponent,
        ProbationDayDeleteDialogComponent,
        ProbationDayDeletePopupComponent,
    ],
    providers: [
        ProbationDayService,
        ProbationDayPopupService,
        ProbationDayResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1ProbationDayModule {}
