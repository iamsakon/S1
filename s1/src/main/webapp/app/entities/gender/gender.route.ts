import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { GenderComponent } from './gender.component';
import { GenderDetailComponent } from './gender-detail.component';
import { GenderPopupComponent } from './gender-dialog.component';
import { GenderDeletePopupComponent } from './gender-delete-dialog.component';

@Injectable()
export class GenderResolvePagingParams implements Resolve<any> {

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

export const genderRoute: Routes = [
    {
        path: 'gender',
        component: GenderComponent,
        resolve: {
            'pagingParams': GenderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.gender.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'gender/:id',
        component: GenderDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.gender.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const genderPopupRoute: Routes = [
    {
        path: 'gender-new',
        component: GenderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.gender.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'gender/:id/edit',
        component: GenderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.gender.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'gender/:id/delete',
        component: GenderDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.gender.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
