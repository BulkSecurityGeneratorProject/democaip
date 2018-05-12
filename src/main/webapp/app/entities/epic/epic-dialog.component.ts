import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Epic } from './epic.model';
import { EpicPopupService } from './epic-popup.service';
import { EpicService } from './epic.service';
import { User, UserService } from '../../shared';
import { Project, ProjectService } from '../project';

@Component({
    selector: 'jhi-epic-dialog',
    templateUrl: './epic-dialog.component.html'
})
export class EpicDialogComponent implements OnInit {

    epic: Epic;
    isSaving: boolean;

    users: User[];

    projects: Project[];
    creationDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private epicService: EpicService,
        private userService: UserService,
        private projectService: ProjectService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.projectService.query()
            .subscribe((res: HttpResponse<Project[]>) => { this.projects = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.epic.id !== undefined) {
            this.subscribeToSaveResponse(
                this.epicService.update(this.epic));
        } else {
            this.subscribeToSaveResponse(
                this.epicService.create(this.epic));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Epic>>) {
        result.subscribe((res: HttpResponse<Epic>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Epic) {
        this.eventManager.broadcast({ name: 'epicListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackProjectById(index: number, item: Project) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-epic-popup',
    template: ''
})
export class EpicPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private epicPopupService: EpicPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.epicPopupService
                    .open(EpicDialogComponent as Component, params['id']);
            } else {
                this.epicPopupService
                    .open(EpicDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
