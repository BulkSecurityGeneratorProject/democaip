import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DemocaipProjectModule } from './project/project.module';
import { DemocaipEpicModule } from './epic/epic.module';
import { DemocaipStoryModule } from './story/story.module';
import { DemocaipTaskModule } from './task/task.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        DemocaipProjectModule,
        DemocaipEpicModule,
        DemocaipStoryModule,
        DemocaipTaskModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DemocaipEntityModule {}
