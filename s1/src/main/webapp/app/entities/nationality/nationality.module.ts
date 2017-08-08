import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S1SharedModule } from '../../shared';
import {
    NationalityService,
    NationalityPopupService,
    NationalityComponent,
    NationalityDetailComponent,
    NationalityDialogComponent,
    NationalityPopupComponent,
    NationalityDeletePopupComponent,
    NationalityDeleteDialogComponent,
    nationalityRoute,
    nationalityPopupRoute,
    NationalityResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...nationalityRoute,
    ...nationalityPopupRoute,
];

@NgModule({
    imports: [
        S1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        NationalityComponent,
        NationalityDetailComponent,
        NationalityDialogComponent,
        NationalityDeleteDialogComponent,
        NationalityPopupComponent,
        NationalityDeletePopupComponent,
    ],
    entryComponents: [
        NationalityComponent,
        NationalityDialogComponent,
        NationalityPopupComponent,
        NationalityDeleteDialogComponent,
        NationalityDeletePopupComponent,
    ],
    providers: [
        NationalityService,
        NationalityPopupService,
        NationalityResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1NationalityModule {}
