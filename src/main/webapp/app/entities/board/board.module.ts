import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WBoardSharedModule } from '../../shared';
import {
    BoardService,
    BoardPopupService,
    BoardComponent,
    BoardDetailComponent,
    BoardDialogComponent,
    BoardPopupComponent,
    BoardDeletePopupComponent,
    BoardDeleteDialogComponent,
    boardRoute,
    boardPopupRoute,
} from './';

const ENTITY_STATES = [
    ...boardRoute,
    ...boardPopupRoute,
];

@NgModule({
    imports: [
        WBoardSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BoardComponent,
        BoardDetailComponent,
        BoardDialogComponent,
        BoardDeleteDialogComponent,
        BoardPopupComponent,
        BoardDeletePopupComponent,
    ],
    entryComponents: [
        BoardComponent,
        BoardDialogComponent,
        BoardPopupComponent,
        BoardDeleteDialogComponent,
        BoardDeletePopupComponent,
    ],
    providers: [
        BoardService,
        BoardPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardBoardModule {}
