import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DemocaipSharedModule } from '../../shared';
import { DemocaipAdminModule } from '../../admin/admin.module';
import {
    EpicService,
    EpicPopupService,
    EpicComponent,
    EpicDetailComponent,
    EpicDialogComponent,
    EpicPopupComponent,
    EpicDeletePopupComponent,
    EpicDeleteDialogComponent,
    epicRoute,
    epicPopupRoute,
} from './';

const ENTITY_STATES = [
    ...epicRoute,
    ...epicPopupRoute,
];

@NgModule({
    imports: [
        DemocaipSharedModule,
        DemocaipAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        EpicComponent,
        EpicDetailComponent,
        EpicDialogComponent,
        EpicDeleteDialogComponent,
        EpicPopupComponent,
        EpicDeletePopupComponent,
    ],
    entryComponents: [
        EpicComponent,
        EpicDialogComponent,
        EpicPopupComponent,
        EpicDeleteDialogComponent,
        EpicDeletePopupComponent,
    ],
    providers: [
        EpicService,
        EpicPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DemocaipEpicModule {}
