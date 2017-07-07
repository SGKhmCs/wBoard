import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { BoardsBodyComponent } from './boards-body.component';
import { BoardsBodyDetailComponent } from './boards-body-detail.component';
import { BoardsBodyPopupComponent } from './boards-body-dialog.component';
import { BoardsBodyDeletePopupComponent } from './boards-body-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class BoardsBodyResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: PaginationUtil) {}

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

export const boardsBodyRoute: Routes = [
    {
        path: 'boards-body',
        component: BoardsBodyComponent,
        resolve: {
            'pagingParams': BoardsBodyResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.boardsBody.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'boards-body/:id',
        component: BoardsBodyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.boardsBody.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const boardsBodyPopupRoute: Routes = [
    {
        path: 'boards-body-new',
        component: BoardsBodyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.boardsBody.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'boards-body/:id/edit',
        component: BoardsBodyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.boardsBody.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'boards-body/:id/delete',
        component: BoardsBodyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.boardsBody.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
