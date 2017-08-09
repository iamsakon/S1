import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S1SharedModule } from '../../shared';
import {
    BloodTypeService,
    BloodTypePopupService,
    BloodTypeComponent,
    BloodTypeDetailComponent,
    BloodTypeDialogComponent,
    BloodTypePopupComponent,
    BloodTypeDeletePopupComponent,
    BloodTypeDeleteDialogComponent,
    bloodTypeRoute,
    bloodTypePopupRoute,
    BloodTypeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...bloodTypeRoute,
    ...bloodTypePopupRoute,
];

@NgModule({
    imports: [
        S1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BloodTypeComponent,
        BloodTypeDetailComponent,
        BloodTypeDialogComponent,
        BloodTypeDeleteDialogComponent,
        BloodTypePopupComponent,
        BloodTypeDeletePopupComponent,
    ],
    entryComponents: [
        BloodTypeComponent,
        BloodTypeDialogComponent,
        BloodTypePopupComponent,
        BloodTypeDeleteDialogComponent,
        BloodTypeDeletePopupComponent,
    ],
    providers: [
        BloodTypeService,
        BloodTypePopupService,
        BloodTypeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1BloodTypeModule {}
