import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WBoardSharedModule } from '../../shared';
import { WBoardAdminModule } from '../../admin/admin.module';
import {
    WriterToolsService,
    WriterToolsPopupService,
    WriterToolsComponent,
    WriterToolsDetailComponent,
    WriterToolsDialogComponent,
    WriterToolsPopupComponent,
    WriterToolsDeletePopupComponent,
    WriterToolsDeleteDialogComponent,
    writerToolsRoute,
    writerToolsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...writerToolsRoute,
    ...writerToolsPopupRoute,
];

@NgModule({
    imports: [
        WBoardSharedModule,
        WBoardAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        WriterToolsComponent,
        WriterToolsDetailComponent,
        WriterToolsDialogComponent,
        WriterToolsDeleteDialogComponent,
        WriterToolsPopupComponent,
        WriterToolsDeletePopupComponent,
    ],
    entryComponents: [
        WriterToolsComponent,
        WriterToolsDialogComponent,
        WriterToolsPopupComponent,
        WriterToolsDeleteDialogComponent,
        WriterToolsDeletePopupComponent,
    ],
    providers: [
        WriterToolsService,
        WriterToolsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardWriterToolsModule {}
