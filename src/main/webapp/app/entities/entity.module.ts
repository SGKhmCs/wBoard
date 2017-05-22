import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WBoardBoardModule } from './board/board.module';
import { WBoardPermissionTypeModule } from './permission-type/permission-type.module';
import { WBoardPermissionModule } from './permission/permission.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        WBoardBoardModule,
        WBoardPermissionTypeModule,
        WBoardPermissionModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WBoardEntityModule {}
