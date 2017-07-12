import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WBoardSharedModule } from '../../shared';
import {
    BoardsBodyService,
    BoardsBodyPopupService,
    BoardsBodyComponent,
    BoardsBodyDetailComponent,
    BoardsBodyDialogComponent,
    BoardsBodyPopupComponent,
    BoardsBodyDeletePopupComponent,
    BoardsBodyDeleteDialogComponent,
    boardsBodyRoute,
    boardsBodyPopupRoute,
    BoardsBodyResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...boardsBodyRoute,
    ...boardsBodyPopupRoute,
];

@NgModule({
    imports: [
        WBoardSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BoardsBodyComponent,
        BoardsBodyDetailComponent,
        BoardsBodyDialogComponent,
        BoardsBodyDeleteDialogComponent,
        BoardsBodyPopupComponent,
        BoardsBodyDeletePopupComponent,
    ],
    entryComponents: [
        BoardsBodyComponent,
        BoardsBodyDialogComponent,
        BoardsBodyPopupComponent,
        BoardsBodyDeleteDialogComponent,
        BoardsBodyDeletePopupComponent,
    ],
    providers: [
        BoardsBodyService,
        BoardsBodyPopupService,
        BoardsBodyResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardBoardsBodyModule {}
