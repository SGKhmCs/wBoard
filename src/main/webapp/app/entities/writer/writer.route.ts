import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { WriterComponent } from './writer.component';
import { WriterDetailComponent } from './writer-detail.component';
import { WriterPopupComponent } from './writer-dialog.component';
import { WriterDeletePopupComponent } from './writer-delete-dialog.component';

import { Principal } from '../../shared';

export const writerRoute: Routes = [
    {
        path: 'writer',
        component: WriterComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.writer.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'writer/:id',
        component: WriterDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.writer.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const writerPopupRoute: Routes = [
    {
        path: 'writer-new',
        component: WriterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.writer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'writer/:id/edit',
        component: WriterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.writer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'writer/:id/delete',
        component: WriterDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'wBoardApp.writer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
