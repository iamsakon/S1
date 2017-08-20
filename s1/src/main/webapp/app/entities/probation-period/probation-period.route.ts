import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProbationPeriodComponent } from './probation-period.component';
import { ProbationPeriodDetailComponent } from './probation-period-detail.component';
import { ProbationPeriodPopupComponent } from './probation-period-dialog.component';
import { ProbationPeriodDeletePopupComponent } from './probation-period-delete-dialog.component';

@Injectable()
export class ProbationPeriodResolvePagingParams implements Resolve<any> {

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

export const probationPeriodRoute: Routes = [
    {
        path: 'probation-period',
        component: ProbationPeriodComponent,
        resolve: {
            'pagingParams': ProbationPeriodResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.probationPeriod.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'probation-period/:id',
        component: ProbationPeriodDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.probationPeriod.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const probationPeriodPopupRoute: Routes = [
    {
        path: 'probation-period-new',
        component: ProbationPeriodPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.probationPeriod.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'probation-period/:id/edit',
        component: ProbationPeriodPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.probationPeriod.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'probation-period/:id/delete',
        component: ProbationPeriodDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.probationPeriod.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
