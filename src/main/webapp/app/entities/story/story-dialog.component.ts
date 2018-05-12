import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Story } from './story.model';
import { StoryPopupService } from './story-popup.service';
import { StoryService } from './story.service';
import { User, UserService } from '../../shared';
import { Epic, EpicService } from '../epic';

@Component({
    selector: 'jhi-story-dialog',
    templateUrl: './story-dialog.component.html'
})
export class StoryDialogComponent implements OnInit {

    story: Story;
    isSaving: boolean;

    users: User[];

    epics: Epic[];
    creationDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private storyService: StoryService,
        private userService: UserService,
        private epicService: EpicService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.epicService.query()
            .subscribe((res: HttpResponse<Epic[]>) => { this.epics = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.story.id !== undefined) {
            this.subscribeToSaveResponse(
                this.storyService.update(this.story));
        } else {
            this.subscribeToSaveResponse(
                this.storyService.create(this.story));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Story>>) {
        result.subscribe((res: HttpResponse<Story>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Story) {
        this.eventManager.broadcast({ name: 'storyListModification', content: 'OK'});
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

    trackEpicById(index: number, item: Epic) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-story-popup',
    template: ''
})
export class StoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private storyPopupService: StoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.storyPopupService
                    .open(StoryDialogComponent as Component, params['id']);
            } else {
                this.storyPopupService
                    .open(StoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
