import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { WriterTools } from './writer-tools.model';
import { WriterToolsService } from './writer-tools.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-writer-tools',
    templateUrl: './writer-tools.component.html'
})
export class WriterToolsComponent implements OnInit, OnDestroy {
writerTools: WriterTools[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private writerToolsService: WriterToolsService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.writerToolsService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.writerTools = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.writerToolsService.query().subscribe(
            (res: ResponseWrapper) => {
                this.writerTools = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInWriterTools();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: WriterTools) {
        return item.id;
    }
    registerChangeInWriterTools() {
        this.eventSubscriber = this.eventManager.subscribe('writerToolsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
