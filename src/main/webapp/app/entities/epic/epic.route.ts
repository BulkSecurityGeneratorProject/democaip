import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { EpicComponent } from './epic.component';
import { EpicDetailComponent } from './epic-detail.component';
import { EpicPopupComponent } from './epic-dialog.component';
import { EpicDeletePopupComponent } from './epic-delete-dialog.component';

export const epicRoute: Routes = [
    {
        path: 'epic',
        component: EpicComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'democaipApp.epic.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'epic/:id',
        component: EpicDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'democaipApp.epic.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const epicPopupRoute: Routes = [
    {
        path: 'epic-new',
        component: EpicPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'democaipApp.epic.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'epic/:id/edit',
        component: EpicPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'democaipApp.epic.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'epic/:id/delete',
        component: EpicDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'democaipApp.epic.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
