import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { PermissionComponent } from './permission.component';
import { PermissionDetailComponent } from './permission-detail.component';
import { PermissionPopupComponent } from './permission-dialog.component';
import { PermissionDeletePopupComponent } from './permission-delete-dialog.component';

import { Principal } from '../../shared';

export const permissionRoute: Routes = [
  {
    path: 'permission',
    component: PermissionComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'wBoardApp.permission.home.title'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'permission/:id',
    component: PermissionDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'wBoardApp.permission.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const permissionPopupRoute: Routes = [
  {
    path: 'permission-new',
    component: PermissionPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'wBoardApp.permission.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'permission/:id/edit',
    component: PermissionPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'wBoardApp.permission.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'permission/:id/delete',
    component: PermissionDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'wBoardApp.permission.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
