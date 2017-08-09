import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { BloodTypeComponent } from './blood-type.component';
import { BloodTypeDetailComponent } from './blood-type-detail.component';
import { BloodTypePopupComponent } from './blood-type-dialog.component';
import { BloodTypeDeletePopupComponent } from './blood-type-delete-dialog.component';

@Injectable()
export class BloodTypeResolvePagingParams implements Resolve<any> {

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

export const bloodTypeRoute: Routes = [
    {
        path: 'blood-type',
        component: BloodTypeComponent,
        resolve: {
            'pagingParams': BloodTypeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.bloodType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'blood-type/:id',
        component: BloodTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.bloodType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bloodTypePopupRoute: Routes = [
    {
        path: 'blood-type-new',
        component: BloodTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.bloodType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'blood-type/:id/edit',
        component: BloodTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.bloodType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'blood-type/:id/delete',
        component: BloodTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.bloodType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
