import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S1SharedModule } from '../../shared';
import {
    GenderService,
    GenderPopupService,
    GenderComponent,
    GenderDetailComponent,
    GenderDialogComponent,
    GenderPopupComponent,
    GenderDeletePopupComponent,
    GenderDeleteDialogComponent,
    genderRoute,
    genderPopupRoute,
    GenderResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...genderRoute,
    ...genderPopupRoute,
];

@NgModule({
    imports: [
        S1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        GenderComponent,
        GenderDetailComponent,
        GenderDialogComponent,
        GenderDeleteDialogComponent,
        GenderPopupComponent,
        GenderDeletePopupComponent,
    ],
    entryComponents: [
        GenderComponent,
        GenderDialogComponent,
        GenderPopupComponent,
        GenderDeleteDialogComponent,
        GenderDeletePopupComponent,
    ],
    providers: [
        GenderService,
        GenderPopupService,
        GenderResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1GenderModule {}
