import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WBoardSharedModule } from '../../shared';
import { WBoardAdminModule } from '../../admin/admin.module';
import {
    AdminToolsService,
    AdminToolsPopupService,
    AdminToolsComponent,
    AdminToolsDetailComponent,
    AdminToolsDialogComponent,
    AdminToolsPopupComponent,
    AdminToolsDeletePopupComponent,
    AdminToolsDeleteDialogComponent,
    adminToolsRoute,
    adminToolsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...adminToolsRoute,
    ...adminToolsPopupRoute,
];

@NgModule({
    imports: [
        WBoardSharedModule,
        WBoardAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AdminToolsComponent,
        AdminToolsDetailComponent,
        AdminToolsDialogComponent,
        AdminToolsDeleteDialogComponent,
        AdminToolsPopupComponent,
        AdminToolsDeletePopupComponent,
    ],
    entryComponents: [
        AdminToolsComponent,
        AdminToolsDialogComponent,
        AdminToolsPopupComponent,
        AdminToolsDeleteDialogComponent,
        AdminToolsDeletePopupComponent,
    ],
    providers: [
        AdminToolsService,
        AdminToolsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardAdminToolsModule {}
