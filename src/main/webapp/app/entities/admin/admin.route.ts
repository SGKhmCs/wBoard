import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { AdminComponent } from './admin.component';
import { AdminDetailComponent } from './admin-detail.component';
import { AdminPopupComponent } from './admin-dialog.component';
import { AdminDeletePopupComponent } from './admin-delete-dialog.component';

import { Principal } from '../../shared';

export const adminRoute: Routes = [
    {
        path: 'admin',
        component: AdminComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.admin.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'admin/:id',
        component: AdminDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.admin.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const adminPopupRoute: Routes = [
    {
        path: 'admin-new',
        component: AdminPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.admin.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'admin/:id/edit',
        component: AdminPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.admin.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'admin/:id/delete',
        component: AdminDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.admin.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
