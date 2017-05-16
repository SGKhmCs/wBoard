import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { PermissionTypeComponent } from './permission-type.component';
import { PermissionTypeDetailComponent } from './permission-type-detail.component';
import { PermissionTypePopupComponent } from './permission-type-dialog.component';
import { PermissionTypeDeletePopupComponent } from './permission-type-delete-dialog.component';

import { Principal } from '../../shared';

export const permissionTypeRoute: Routes = [
    {
        path: 'permission-type',
        component: PermissionTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.permissionType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'permission-type/:id',
        component: PermissionTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.permissionType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const permissionTypePopupRoute: Routes = [
    {
        path: 'permission-type-new',
        component: PermissionTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.permissionType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'permission-type/:id/edit',
        component: PermissionTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.permissionType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'permission-type/:id/delete',
        component: PermissionTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.permissionType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
