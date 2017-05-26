import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WBoardSharedModule } from '../../shared';
import { WBoardAdminModule } from '../../admin/admin.module';
import {
    BoardUserService,
    BoardUserPopupService,
    BoardUserComponent,
    BoardUserDetailComponent,
    BoardUserDialogComponent,
    BoardUserPopupComponent,
    BoardUserDeletePopupComponent,
    BoardUserDeleteDialogComponent,
    boardUserRoute,
    boardUserPopupRoute,
} from './';

const ENTITY_STATES = [
    ...boardUserRoute,
    ...boardUserPopupRoute,
];

@NgModule({
    imports: [
        WBoardSharedModule,
        WBoardAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BoardUserComponent,
        BoardUserDetailComponent,
        BoardUserDialogComponent,
        BoardUserDeleteDialogComponent,
        BoardUserPopupComponent,
        BoardUserDeletePopupComponent,
    ],
    entryComponents: [
        BoardUserComponent,
        BoardUserDialogComponent,
        BoardUserPopupComponent,
        BoardUserDeleteDialogComponent,
        BoardUserDeletePopupComponent,
    ],
    providers: [
        BoardUserService,
        BoardUserPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardBoardUserModule {}
