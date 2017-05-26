import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ReaderToolsComponent } from './reader-tools.component';
import { ReaderToolsDetailComponent } from './reader-tools-detail.component';
import { ReaderToolsPopupComponent } from './reader-tools-dialog.component';
import { ReaderToolsDeletePopupComponent } from './reader-tools-delete-dialog.component';

import { Principal } from '../../shared';

export const readerToolsRoute: Routes = [
    {
        path: 'reader-tools',
        component: ReaderToolsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.readerTools.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'reader-tools/:id',
        component: ReaderToolsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.readerTools.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const readerToolsPopupRoute: Routes = [
    {
        path: 'reader-tools-new',
        component: ReaderToolsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.readerTools.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'reader-tools/:id/edit',
        component: ReaderToolsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.readerTools.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'reader-tools/:id/delete',
        component: ReaderToolsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.readerTools.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
