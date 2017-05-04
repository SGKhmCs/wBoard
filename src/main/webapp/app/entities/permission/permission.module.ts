import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WBoardSharedModule } from '../../shared';
import { WBoardAdminModule } from '../../admin/admin.module';
import {
    PermissionService,
    PermissionPopupService,
    PermissionComponent,
    PermissionDetailComponent,
    PermissionDialogComponent,
    PermissionPopupComponent,
    PermissionDeletePopupComponent,
    PermissionDeleteDialogComponent,
    permissionRoute,
    permissionPopupRoute,
} from './';

const ENTITY_STATES = [
    ...permissionRoute,
    ...permissionPopupRoute,
];

@NgModule({
    imports: [
        WBoardSharedModule,
        WBoardAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PermissionComponent,
        PermissionDetailComponent,
        PermissionDialogComponent,
        PermissionDeleteDialogComponent,
        PermissionPopupComponent,
        PermissionDeletePopupComponent,
    ],
    entryComponents: [
        PermissionComponent,
        PermissionDialogComponent,
        PermissionPopupComponent,
        PermissionDeleteDialogComponent,
        PermissionDeletePopupComponent,
    ],
    providers: [
        PermissionService,
        PermissionPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardPermissionModule {}
