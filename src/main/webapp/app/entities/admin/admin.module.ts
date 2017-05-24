import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WBoardSharedModule } from '../../shared';
import {
    AdminService,
    AdminPopupService,
    AdminComponent,
    AdminDetailComponent,
    AdminDialogComponent,
    AdminPopupComponent,
    AdminDeletePopupComponent,
    AdminDeleteDialogComponent,
    adminRoute,
    adminPopupRoute,
} from './';

const ENTITY_STATES = [
    ...adminRoute,
    ...adminPopupRoute,
];

@NgModule({
    imports: [
        WBoardSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AdminComponent,
        AdminDetailComponent,
        AdminDialogComponent,
        AdminDeleteDialogComponent,
        AdminPopupComponent,
        AdminDeletePopupComponent,
    ],
    entryComponents: [
        AdminComponent,
        AdminDialogComponent,
        AdminPopupComponent,
        AdminDeleteDialogComponent,
        AdminDeletePopupComponent,
    ],
    providers: [
        AdminService,
        AdminPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardAdminModule {}
