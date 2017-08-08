import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S1SharedModule } from '../../shared';
import {
    PrefixService,
    PrefixPopupService,
    PrefixComponent,
    PrefixDetailComponent,
    PrefixDialogComponent,
    PrefixPopupComponent,
    PrefixDeletePopupComponent,
    PrefixDeleteDialogComponent,
    prefixRoute,
    prefixPopupRoute,
    PrefixResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...prefixRoute,
    ...prefixPopupRoute,
];

@NgModule({
    imports: [
        S1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PrefixComponent,
        PrefixDetailComponent,
        PrefixDialogComponent,
        PrefixDeleteDialogComponent,
        PrefixPopupComponent,
        PrefixDeletePopupComponent,
    ],
    entryComponents: [
        PrefixComponent,
        PrefixDialogComponent,
        PrefixPopupComponent,
        PrefixDeleteDialogComponent,
        PrefixDeletePopupComponent,
    ],
    providers: [
        PrefixService,
        PrefixPopupService,
        PrefixResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1PrefixModule {}
