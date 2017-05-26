import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { WriterToolsComponent } from './writer-tools.component';
import { WriterToolsDetailComponent } from './writer-tools-detail.component';
import { WriterToolsPopupComponent } from './writer-tools-dialog.component';
import { WriterToolsDeletePopupComponent } from './writer-tools-delete-dialog.component';

import { Principal } from '../../shared';

export const writerToolsRoute: Routes = [
    {
        path: 'writer-tools',
        component: WriterToolsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.writerTools.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'writer-tools/:id',
        component: WriterToolsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.writerTools.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const writerToolsPopupRoute: Routes = [
    {
        path: 'writer-tools-new',
        component: WriterToolsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.writerTools.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'writer-tools/:id/edit',
        component: WriterToolsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.writerTools.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'writer-tools/:id/delete',
        component: WriterToolsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.writerTools.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
