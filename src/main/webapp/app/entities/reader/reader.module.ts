import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WBoardSharedModule } from '../../shared';
import {
    ReaderService,
    ReaderPopupService,
    ReaderComponent,
    ReaderDetailComponent,
    ReaderDialogComponent,
    ReaderPopupComponent,
    ReaderDeletePopupComponent,
    ReaderDeleteDialogComponent,
    readerRoute,
    readerPopupRoute,
} from './';

const ENTITY_STATES = [
    ...readerRoute,
    ...readerPopupRoute,
];

@NgModule({
    imports: [
        WBoardSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ReaderComponent,
        ReaderDetailComponent,
        ReaderDialogComponent,
        ReaderDeleteDialogComponent,
        ReaderPopupComponent,
        ReaderDeletePopupComponent,
    ],
    entryComponents: [
        ReaderComponent,
        ReaderDialogComponent,
        ReaderPopupComponent,
        ReaderDeleteDialogComponent,
        ReaderDeletePopupComponent,
    ],
    providers: [
        ReaderService,
        ReaderPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardReaderModule {}
