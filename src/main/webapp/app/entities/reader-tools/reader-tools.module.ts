import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WBoardSharedModule } from '../../shared';
import { WBoardAdminModule } from '../../admin/admin.module';
import {
    ReaderToolsService,
    ReaderToolsPopupService,
    ReaderToolsComponent,
    ReaderToolsDetailComponent,
    ReaderToolsDialogComponent,
    ReaderToolsPopupComponent,
    ReaderToolsDeletePopupComponent,
    ReaderToolsDeleteDialogComponent,
    readerToolsRoute,
    readerToolsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...readerToolsRoute,
    ...readerToolsPopupRoute,
];

@NgModule({
    imports: [
        WBoardSharedModule,
        WBoardAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ReaderToolsComponent,
        ReaderToolsDetailComponent,
        ReaderToolsDialogComponent,
        ReaderToolsDeleteDialogComponent,
        ReaderToolsPopupComponent,
        ReaderToolsDeletePopupComponent,
    ],
    entryComponents: [
        ReaderToolsComponent,
        ReaderToolsDialogComponent,
        ReaderToolsPopupComponent,
        ReaderToolsDeleteDialogComponent,
        ReaderToolsDeletePopupComponent,
    ],
    providers: [
        ReaderToolsService,
        ReaderToolsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardReaderToolsModule {}
