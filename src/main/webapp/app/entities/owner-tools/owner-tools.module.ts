import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WBoardSharedModule } from '../../shared';
import { WBoardAdminModule } from '../../admin/admin.module';
import {
    OwnerToolsService,
    OwnerToolsPopupService,
    OwnerToolsComponent,
    OwnerToolsDetailComponent,
    OwnerToolsDialogComponent,
    OwnerToolsPopupComponent,
    OwnerToolsDeletePopupComponent,
    OwnerToolsDeleteDialogComponent,
    ownerToolsRoute,
    ownerToolsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...ownerToolsRoute,
    ...ownerToolsPopupRoute,
];

@NgModule({
    imports: [
        WBoardSharedModule,
        WBoardAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        OwnerToolsComponent,
        OwnerToolsDetailComponent,
        OwnerToolsDialogComponent,
        OwnerToolsDeleteDialogComponent,
        OwnerToolsPopupComponent,
        OwnerToolsDeletePopupComponent,
    ],
    entryComponents: [
        OwnerToolsComponent,
        OwnerToolsDialogComponent,
        OwnerToolsPopupComponent,
        OwnerToolsDeleteDialogComponent,
        OwnerToolsDeletePopupComponent,
    ],
    providers: [
        OwnerToolsService,
        OwnerToolsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardOwnerToolsModule {}
