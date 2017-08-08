import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MilitaryStatusComponent } from './military-status.component';
import { MilitaryStatusDetailComponent } from './military-status-detail.component';
import { MilitaryStatusPopupComponent } from './military-status-dialog.component';
import { MilitaryStatusDeletePopupComponent } from './military-status-delete-dialog.component';

@Injectable()
export class MilitaryStatusResolvePagingParams implements Resolve<any> {

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

export const militaryStatusRoute: Routes = [
    {
        path: 'military-status',
        component: MilitaryStatusComponent,
        resolve: {
            'pagingParams': MilitaryStatusResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.militaryStatus.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'military-status/:id',
        component: MilitaryStatusDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.militaryStatus.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const militaryStatusPopupRoute: Routes = [
    {
        path: 'military-status-new',
        component: MilitaryStatusPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.militaryStatus.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'military-status/:id/edit',
        component: MilitaryStatusPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.militaryStatus.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'military-status/:id/delete',
        component: MilitaryStatusDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.militaryStatus.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
