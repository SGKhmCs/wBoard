import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { BoardUserComponent } from './board-user.component';
import { BoardUserDetailComponent } from './board-user-detail.component';
import { BoardUserPopupComponent } from './board-user-dialog.component';
import { BoardUserDeletePopupComponent } from './board-user-delete-dialog.component';

import { Principal } from '../../shared';

export const boardUserRoute: Routes = [
    {
        path: 'board-user',
        component: BoardUserComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.boardUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'board-user/:id',
        component: BoardUserDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.boardUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const boardUserPopupRoute: Routes = [
    {
        path: 'board-user-new',
        component: BoardUserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.boardUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'board-user/:id/edit',
        component: BoardUserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.boardUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'board-user/:id/delete',
        component: BoardUserDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.boardUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
