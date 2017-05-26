import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { AdminToolsComponent } from './admin-tools.component';
import { AdminToolsDetailComponent } from './admin-tools-detail.component';
import { AdminToolsPopupComponent } from './admin-tools-dialog.component';
import { AdminToolsDeletePopupComponent } from './admin-tools-delete-dialog.component';

import { Principal } from '../../shared';

export const adminToolsRoute: Routes = [
    {
        path: 'admin-tools',
        component: AdminToolsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.adminTools.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'admin-tools/:id',
        component: AdminToolsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.adminTools.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const adminToolsPopupRoute: Routes = [
    {
        path: 'admin-tools-new',
        component: AdminToolsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.adminTools.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'admin-tools/:id/edit',
        component: AdminToolsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.adminTools.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'admin-tools/:id/delete',
        component: AdminToolsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.adminTools.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
