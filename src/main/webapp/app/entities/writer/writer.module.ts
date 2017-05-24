import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WBoardSharedModule } from '../../shared';
import {
    WriterService,
    WriterPopupService,
    WriterComponent,
    WriterDetailComponent,
    WriterDialogComponent,
    WriterPopupComponent,
    WriterDeletePopupComponent,
    WriterDeleteDialogComponent,
    writerRoute,
    writerPopupRoute,
} from './';

const ENTITY_STATES = [
    ...writerRoute,
    ...writerPopupRoute,
];

@NgModule({
    imports: [
        WBoardSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        WriterComponent,
        WriterDetailComponent,
        WriterDialogComponent,
        WriterDeleteDialogComponent,
        WriterPopupComponent,
        WriterDeletePopupComponent,
    ],
    entryComponents: [
        WriterComponent,
        WriterDialogComponent,
        WriterPopupComponent,
        WriterDeleteDialogComponent,
        WriterDeletePopupComponent,
    ],
    providers: [
        WriterService,
        WriterPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardWriterModule {}
