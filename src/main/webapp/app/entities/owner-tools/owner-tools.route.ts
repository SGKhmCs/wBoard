import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { OwnerToolsComponent } from './owner-tools.component';
import { OwnerToolsDetailComponent } from './owner-tools-detail.component';
import { OwnerToolsPopupComponent } from './owner-tools-dialog.component';
import { OwnerToolsDeletePopupComponent } from './owner-tools-delete-dialog.component';

import { Principal } from '../../shared';

export const ownerToolsRoute: Routes = [
    {
        path: 'owner-tools',
        component: OwnerToolsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.ownerTools.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'owner-tools/:id',
        component: OwnerToolsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.ownerTools.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ownerToolsPopupRoute: Routes = [
    {
        path: 'owner-tools-new',
        component: OwnerToolsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.ownerTools.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'owner-tools/:id/edit',
        component: OwnerToolsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.ownerTools.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'owner-tools/:id/delete',
        component: OwnerToolsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.ownerTools.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
