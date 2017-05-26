import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WBoardBoardModule } from './board/board.module';
import { WBoardBoardUserModule } from './board-user/board-user.module';
import { WBoardReaderModule } from './reader/reader.module';
import { WBoardWriterModule } from './writer/writer.module';
import { WBoardAdminModule } from './admin/admin.module';
import { WBoardAdminToolsModule } from './admin-tools/admin-tools.module';
import { WBoardWriterToolsModule } from './writer-tools/writer-tools.module';
import { WBoardReaderToolsModule } from './reader-tools/reader-tools.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        WBoardBoardModule,
        WBoardBoardUserModule,
        WBoardReaderModule,
        WBoardWriterModule,
        WBoardAdminModule,
        WBoardAdminToolsModule,
        WBoardWriterToolsModule,
        WBoardReaderToolsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardEntityModule {}
