import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { EmployeeContactComponent } from './employee-contact.component';
import { EmployeeContactDetailComponent } from './employee-contact-detail.component';
import { EmployeeContactPopupComponent } from './employee-contact-dialog.component';
import { EmployeeContactDeletePopupComponent } from './employee-contact-delete-dialog.component';

@Injectable()
export class EmployeeContactResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const employeeContactRoute: Routes = [
    {
        path: 'employee-contact',
        component: EmployeeContactComponent,
        resolve: {
            'pagingParams': EmployeeContactResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.employeeContact.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'employee-contact/:id',
        component: EmployeeContactDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.employeeContact.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const employeeContactPopupRoute: Routes = [
    {
        path: 'employee-contact-new',
        component: EmployeeContactPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.employeeContact.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'employee-contact/:id/edit',
        component: EmployeeContactPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.employeeContact.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'employee-contact/:id/delete',
        component: EmployeeContactDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.employeeContact.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
