import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S1SharedModule } from '../../shared';
import {
    RaceService,
    RacePopupService,
    RaceComponent,
    RaceDetailComponent,
    RaceDialogComponent,
    RacePopupComponent,
    RaceDeletePopupComponent,
    RaceDeleteDialogComponent,
    raceRoute,
    racePopupRoute,
    RaceResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...raceRoute,
    ...racePopupRoute,
];

@NgModule({
    imports: [
        S1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RaceComponent,
        RaceDetailComponent,
        RaceDialogComponent,
        RaceDeleteDialogComponent,
        RacePopupComponent,
        RaceDeletePopupComponent,
    ],
    entryComponents: [
        RaceComponent,
        RaceDialogComponent,
        RacePopupComponent,
        RaceDeleteDialogComponent,
        RaceDeletePopupComponent,
    ],
    providers: [
        RaceService,
        RacePopupService,
        RaceResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1RaceModule {}
