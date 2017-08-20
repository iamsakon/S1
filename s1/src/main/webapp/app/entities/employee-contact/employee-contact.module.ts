import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S1SharedModule } from '../../shared';
import {
    EmployeeContactService,
    EmployeeContactPopupService,
    EmployeeContactComponent,
    EmployeeContactDetailComponent,
    EmployeeContactDialogComponent,
    EmployeeContactPopupComponent,
    EmployeeContactDeletePopupComponent,
    EmployeeContactDeleteDialogComponent,
    employeeContactRoute,
    employeeContactPopupRoute,
    EmployeeContactResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...employeeContactRoute,
    ...employeeContactPopupRoute,
];

@NgModule({
    imports: [
        S1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        EmployeeContactComponent,
        EmployeeContactDetailComponent,
        EmployeeContactDialogComponent,
        EmployeeContactDeleteDialogComponent,
        EmployeeContactPopupComponent,
        EmployeeContactDeletePopupComponent,
    ],
    entryComponents: [
        EmployeeContactComponent,
        EmployeeContactDialogComponent,
        EmployeeContactPopupComponent,
        EmployeeContactDeleteDialogComponent,
        EmployeeContactDeletePopupComponent,
    ],
    providers: [
        EmployeeContactService,
        EmployeeContactPopupService,
        EmployeeContactResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1EmployeeContactModule {}
