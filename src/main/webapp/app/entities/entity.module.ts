import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WBoardAdminToolsModule } from './admin-tools/admin-tools.module';
import { WBoardBoardModule } from './board/board.module';
import { WBoardOwnerToolsModule } from './owner-tools/owner-tools.module';
import { WBoardReaderToolsModule } from './reader-tools/reader-tools.module';
import { WBoardWriterToolsModule } from './writer-tools/writer-tools.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        WBoardAdminToolsModule,
        WBoardBoardModule,
        WBoardOwnerToolsModule,
        WBoardReaderToolsModule,
        WBoardWriterToolsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardEntityModule {}
