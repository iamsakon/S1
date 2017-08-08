import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S1SharedModule } from '../../shared';
import {
    MaritalStatusService,
    MaritalStatusPopupService,
    MaritalStatusComponent,
    MaritalStatusDetailComponent,
    MaritalStatusDialogComponent,
    MaritalStatusPopupComponent,
    MaritalStatusDeletePopupComponent,
    MaritalStatusDeleteDialogComponent,
    maritalStatusRoute,
    maritalStatusPopupRoute,
    MaritalStatusResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...maritalStatusRoute,
    ...maritalStatusPopupRoute,
];

@NgModule({
    imports: [
        S1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MaritalStatusComponent,
        MaritalStatusDetailComponent,
        MaritalStatusDialogComponent,
        MaritalStatusDeleteDialogComponent,
        MaritalStatusPopupComponent,
        MaritalStatusDeletePopupComponent,
    ],
    entryComponents: [
        MaritalStatusComponent,
        MaritalStatusDialogComponent,
        MaritalStatusPopupComponent,
        MaritalStatusDeleteDialogComponent,
        MaritalStatusDeletePopupComponent,
    ],
    providers: [
        MaritalStatusService,
        MaritalStatusPopupService,
        MaritalStatusResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1MaritalStatusModule {}
