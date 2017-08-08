import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S1SharedModule } from '../../shared';
import {
    MilitaryStatusService,
    MilitaryStatusPopupService,
    MilitaryStatusComponent,
    MilitaryStatusDetailComponent,
    MilitaryStatusDialogComponent,
    MilitaryStatusPopupComponent,
    MilitaryStatusDeletePopupComponent,
    MilitaryStatusDeleteDialogComponent,
    militaryStatusRoute,
    militaryStatusPopupRoute,
    MilitaryStatusResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...militaryStatusRoute,
    ...militaryStatusPopupRoute,
];

@NgModule({
    imports: [
        S1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MilitaryStatusComponent,
        MilitaryStatusDetailComponent,
        MilitaryStatusDialogComponent,
        MilitaryStatusDeleteDialogComponent,
        MilitaryStatusPopupComponent,
        MilitaryStatusDeletePopupComponent,
    ],
    entryComponents: [
        MilitaryStatusComponent,
        MilitaryStatusDialogComponent,
        MilitaryStatusPopupComponent,
        MilitaryStatusDeleteDialogComponent,
        MilitaryStatusDeletePopupComponent,
    ],
    providers: [
        MilitaryStatusService,
        MilitaryStatusPopupService,
        MilitaryStatusResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1MilitaryStatusModule {}
