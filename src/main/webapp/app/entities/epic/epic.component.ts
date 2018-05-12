import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Epic } from './epic.model';
import { EpicService } from './epic.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-epic',
    templateUrl: './epic.component.html'
})
export class EpicComponent implements OnInit, OnDestroy {
epics: Epic[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private epicService: EpicService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.epicService.query().subscribe(
            (res: HttpResponse<Epic[]>) => {
                this.epics = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInEpics();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Epic) {
        return item.id;
    }
    registerChangeInEpics() {
        this.eventSubscriber = this.eventManager.subscribe('epicListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
