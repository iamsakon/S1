import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { NationalityComponent } from './nationality.component';
import { NationalityDetailComponent } from './nationality-detail.component';
import { NationalityPopupComponent } from './nationality-dialog.component';
import { NationalityDeletePopupComponent } from './nationality-delete-dialog.component';

@Injectable()
export class NationalityResolvePagingParams implements Resolve<any> {

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

export const nationalityRoute: Routes = [
    {
        path: 'nationality',
        component: NationalityComponent,
        resolve: {
            'pagingParams': NationalityResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.nationality.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'nationality/:id',
        component: NationalityDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.nationality.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const nationalityPopupRoute: Routes = [
    {
        path: 'nationality-new',
        component: NationalityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.nationality.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'nationality/:id/edit',
        component: NationalityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.nationality.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'nationality/:id/delete',
        component: NationalityDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.nationality.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
