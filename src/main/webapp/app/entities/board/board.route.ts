import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { BoardComponent } from './board.component';
import { BoardDetailComponent } from './board-detail.component';
import { BoardPopupComponent } from './board-dialog.component';
import { BoardDeletePopupComponent } from './board-delete-dialog.component';

import { Principal } from '../../shared';

export const boardRoute: Routes = [
    {
        path: 'board',
        component: BoardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.board.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'board/:id',
        component: BoardDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.board.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const boardPopupRoute: Routes = [
    {
        path: 'board-new',
        component: BoardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.board.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'board/:id/edit',
        component: BoardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.board.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'board/:id/delete',
        component: BoardDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.board.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
