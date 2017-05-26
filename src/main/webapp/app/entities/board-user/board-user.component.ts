import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { BoardUser } from './board-user.model';
import { BoardUserService } from './board-user.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-board-user',
    templateUrl: './board-user.component.html'
})
export class BoardUserComponent implements OnInit, OnDestroy {
boardUsers: BoardUser[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private boardUserService: BoardUserService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.boardUserService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.boardUsers = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.boardUserService.query().subscribe(
            (res: ResponseWrapper) => {
                this.boardUsers = res.json;
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
        this.registerChangeInBoardUsers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: BoardUser) {
        return item.id;
    }
    registerChangeInBoardUsers() {
        this.eventSubscriber = this.eventManager.subscribe('boardUserListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
