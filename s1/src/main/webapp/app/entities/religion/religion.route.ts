import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ReligionComponent } from './religion.component';
import { ReligionDetailComponent } from './religion-detail.component';
import { ReligionPopupComponent } from './religion-dialog.component';
import { ReligionDeletePopupComponent } from './religion-delete-dialog.component';

@Injectable()
export class ReligionResolvePagingParams implements Resolve<any> {

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

export const religionRoute: Routes = [
    {
        path: 'religion',
        component: ReligionComponent,
        resolve: {
            'pagingParams': ReligionResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.religion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'religion/:id',
        component: ReligionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.religion.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const religionPopupRoute: Routes = [
    {
        path: 'religion-new',
        component: ReligionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.religion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'religion/:id/edit',
        component: ReligionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.religion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'religion/:id/delete',
        component: ReligionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.religion.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
