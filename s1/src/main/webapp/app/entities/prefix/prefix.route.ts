import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PrefixComponent } from './prefix.component';
import { PrefixDetailComponent } from './prefix-detail.component';
import { PrefixPopupComponent } from './prefix-dialog.component';
import { PrefixDeletePopupComponent } from './prefix-delete-dialog.component';

@Injectable()
export class PrefixResolvePagingParams implements Resolve<any> {

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

export const prefixRoute: Routes = [
    {
        path: 'prefix',
        component: PrefixComponent,
        resolve: {
            'pagingParams': PrefixResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.prefix.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'prefix/:id',
        component: PrefixDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.prefix.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const prefixPopupRoute: Routes = [
    {
        path: 'prefix-new',
        component: PrefixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.prefix.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'prefix/:id/edit',
        component: PrefixPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.prefix.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'prefix/:id/delete',
        component: PrefixDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.prefix.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
