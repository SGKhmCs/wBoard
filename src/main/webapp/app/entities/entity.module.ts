import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WBoardAdminModule } from './admin/admin.module';
import { WBoardBoardModule } from './board/board.module';
import { WBoardReaderModule } from './reader/reader.module';
import { WBoardWriterModule } from './writer/writer.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        WBoardAdminModule,
        WBoardBoardModule,
        WBoardReaderModule,
        WBoardWriterModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardEntityModule {}
