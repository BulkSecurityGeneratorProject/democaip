import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Story } from './story.model';
import { StoryService } from './story.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-story',
    templateUrl: './story.component.html'
})
export class StoryComponent implements OnInit, OnDestroy {
stories: Story[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private storyService: StoryService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.storyService.query().subscribe(
            (res: HttpResponse<Story[]>) => {
                this.stories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInStories();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Story) {
        return item.id;
    }
    registerChangeInStories() {
        this.eventSubscriber = this.eventManager.subscribe('storyListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
