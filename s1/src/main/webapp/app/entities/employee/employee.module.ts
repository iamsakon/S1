import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { S1SharedModule } from '../../shared';
import {
    EmployeeService,
    EmployeePopupService,
    EmployeeComponent,
    EmployeeDetailComponent,
    EmployeeDialogComponent,
    EmployeePopupComponent,
    EmployeeDeletePopupComponent,
    EmployeeDeleteDialogComponent,
    employeeRoute,
    employeePopupRoute,
    EmployeeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...employeeRoute,
    ...employeePopupRoute,
];

@NgModule({
    imports: [
        S1SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        EmployeeComponent,
        EmployeeDetailComponent,
        EmployeeDialogComponent,
        EmployeeDeleteDialogComponent,
        EmployeePopupComponent,
        EmployeeDeletePopupComponent,
    ],
    entryComponents: [
        EmployeeComponent,
        EmployeeDialogComponent,
        EmployeePopupComponent,
        EmployeeDeleteDialogComponent,
        EmployeeDeletePopupComponent,
    ],
    providers: [
        EmployeeService,
        EmployeePopupService,
        EmployeeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class S1EmployeeModule {}
