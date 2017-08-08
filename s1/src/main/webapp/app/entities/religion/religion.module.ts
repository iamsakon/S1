import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S1SharedModule } from '../../shared';
import {
    ReligionService,
    ReligionPopupService,
    ReligionComponent,
    ReligionDetailComponent,
    ReligionDialogComponent,
    ReligionPopupComponent,
    ReligionDeletePopupComponent,
    ReligionDeleteDialogComponent,
    religionRoute,
    religionPopupRoute,
    ReligionResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...religionRoute,
    ...religionPopupRoute,
];

@NgModule({
    imports: [
        S1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ReligionComponent,
        ReligionDetailComponent,
        ReligionDialogComponent,
        ReligionDeleteDialogComponent,
        ReligionPopupComponent,
        ReligionDeletePopupComponent,
    ],
    entryComponents: [
        ReligionComponent,
        ReligionDialogComponent,
        ReligionPopupComponent,
        ReligionDeleteDialogComponent,
        ReligionDeletePopupComponent,
    ],
    providers: [
        ReligionService,
        ReligionPopupService,
        ReligionResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1ReligionModule {}
