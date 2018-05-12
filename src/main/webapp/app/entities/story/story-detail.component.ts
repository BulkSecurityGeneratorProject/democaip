import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Story } from './story.model';
import { StoryService } from './story.service';

@Component({
    selector: 'jhi-story-detail',
    templateUrl: './story-detail.component.html'
})
export class StoryDetailComponent implements OnInit, OnDestroy {

    story: Story;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private storyService: StoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStories();
    }

    load(id) {
        this.storyService.find(id)
            .subscribe((storyResponse: HttpResponse<Story>) => {
                this.story = storyResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'storyListModification',
            (response) => this.load(this.story.id)
        );
    }
}
