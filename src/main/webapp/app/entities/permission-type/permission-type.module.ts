import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WBoardSharedModule } from '../../shared';
import {
    PermissionTypeService,
    PermissionTypePopupService,
    PermissionTypeComponent,
    PermissionTypeDetailComponent,
    PermissionTypeDialogComponent,
    PermissionTypePopupComponent,
    PermissionTypeDeletePopupComponent,
    PermissionTypeDeleteDialogComponent,
    permissionTypeRoute,
    permissionTypePopupRoute,
} from './';

const ENTITY_STATES = [
    ...permissionTypeRoute,
    ...permissionTypePopupRoute,
];

@NgModule({
    imports: [
        WBoardSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PermissionTypeComponent,
        PermissionTypeDetailComponent,
        PermissionTypeDialogComponent,
        PermissionTypeDeleteDialogComponent,
        PermissionTypePopupComponent,
        PermissionTypeDeletePopupComponent,
    ],
    entryComponents: [
        PermissionTypeComponent,
        PermissionTypeDialogComponent,
        PermissionTypePopupComponent,
        PermissionTypeDeleteDialogComponent,
        PermissionTypeDeletePopupComponent,
    ],
    providers: [
        PermissionTypeService,
        PermissionTypePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardPermissionTypeModule {}
