import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { ReaderTools } from './reader-tools.model';
import { ReaderToolsService } from './reader-tools.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-reader-tools',
    templateUrl: './reader-tools.component.html'
})
export class ReaderToolsComponent implements OnInit, OnDestroy {
readerTools: ReaderTools[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private readerToolsService: ReaderToolsService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.readerToolsService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.readerTools = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.readerToolsService.query().subscribe(
            (res: ResponseWrapper) => {
                this.readerTools = res.json;
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
        this.registerChangeInReaderTools();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ReaderTools) {
        return item.id;
    }
    registerChangeInReaderTools() {
        this.eventSubscriber = this.eventManager.subscribe('readerToolsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
