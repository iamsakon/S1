import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProbationDayComponent } from './probation-day.component';
import { ProbationDayDetailComponent } from './probation-day-detail.component';
import { ProbationDayPopupComponent } from './probation-day-dialog.component';
import { ProbationDayDeletePopupComponent } from './probation-day-delete-dialog.component';

@Injectable()
export class ProbationDayResolvePagingParams implements Resolve<any> {

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

export const probationDayRoute: Routes = [
    {
        path: 'probation-day',
        component: ProbationDayComponent,
        resolve: {
            'pagingParams': ProbationDayResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.probationDay.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'probation-day/:id',
        component: ProbationDayDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.probationDay.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const probationDayPopupRoute: Routes = [
    {
        path: 'probation-day-new',
        component: ProbationDayPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.probationDay.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'probation-day/:id/edit',
        component: ProbationDayPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.probationDay.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'probation-day/:id/delete',
        component: ProbationDayDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 's1App.probationDay.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
