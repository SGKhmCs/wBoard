import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ReaderComponent } from './reader.component';
import { ReaderDetailComponent } from './reader-detail.component';
import { ReaderPopupComponent } from './reader-dialog.component';
import { ReaderDeletePopupComponent } from './reader-delete-dialog.component';

import { Principal } from '../../shared';

export const readerRoute: Routes = [
    {
        path: 'reader',
        component: ReaderComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.reader.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'reader/:id',
        component: ReaderDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.reader.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const readerPopupRoute: Routes = [
    {
        path: 'reader-new',
        component: ReaderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.reader.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'reader/:id/edit',
        component: ReaderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.reader.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'reader/:id/delete',
        component: ReaderDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.reader.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
